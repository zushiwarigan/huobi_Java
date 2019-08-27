package com.huobi.client.impl;

import java.io.IOException;
import java.net.URI;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import com.huobi.client.SubscribeOption;
import com.huobi.client.base.CommonConstant;
import com.huobi.client.exception.HuobiApiException;
import com.huobi.client.message.PongMessage;
import com.huobi.client.utils.InternalUtils;
import com.huobi.client.utils.TimeService;
import com.huobi.client.utils.UrlParamsBuilder;
import com.huobi.gateway.EventDecoder;
import com.huobi.gateway.EventDecoder.R;
import com.huobi.gateway.constant.Const;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;


@Slf4j
public class WSConnection extends WebSocketListener {

  private static int connectionCounter = 0;

  public enum ConnectionState {
    IDLE,
    DELAY_CONNECT,
    CONNECTED,
    CLOSED_ON_ERROR
  }

  /**
   * 1000 indicates a normal closure, meaning that the purpose for which the connection was established has been fulfilled.
   */
  private static final int CLOSE_CODE_1000 = 1000;
  /**
   * 1001 indicates that an endpoint is "going away", such as a server going down or a browser having navigated away from a page.
   */
  private static final int CLOSE_CODE_1001 = 1001;
  /**
   * 1002 indicates that an endpoint is terminating the connection due to a protocol error.
   */
  private static final int CLOSE_CODE_1002 = 1002;

  private WebSocket webSocket = null;

  @Getter
  private volatile long lastReceivedTime = 0;

  @Getter
  private volatile ConnectionState state = ConnectionState.IDLE;

  private int delayInSecond = 0;

  private final WSRequest request;
  private final Request okhttpRequest;
  private final String apiKey;
  private final String secretKey;
  private final WSWatchDog watchDog;

  @Getter
  private final int connectionId;

  private String subscriptionMarketUrl = "ws://huobi-gateway.test-12.huobiapps.com/ws";
  private String subscriptionTradingUrl = "ws://huobi-gateway.test-12.huobiapps.com/ws/v1";
  private String tradingHost;
  private String scheme = "ws://";
  private String marketUrl = "/ws";
  private String tradingUrl = "/ws/v1";
  private String apiUrl = "/api/ws";

  public WSConnection(
      String apiKey,
      String secretKey,
      SubscribeOption options,
      WSRequest request,
      WSWatchDog watchDog) {
    this.connectionId = WSConnection.connectionCounter++;
    this.apiKey = apiKey;
    this.secretKey = secretKey;
    this.request = request;
    try {
      String host = new URI(options.getUri()).getHost();
      this.tradingHost = host;
//      if (host.indexOf("api") == 0) {
      this.subscriptionMarketUrl = scheme + host + marketUrl;
      this.subscriptionTradingUrl = scheme + host + tradingUrl;
//      } else {
//        this.subscriptionMarketUrl = scheme + host + apiUrl;
//        this.subscriptionTradingUrl = scheme + host + tradingUrl;
//      }
    } catch (Exception e) {
      log.info(e.getMessage());
    }

    this.okhttpRequest = request.authHandler == null
        ? new Request.Builder().url(subscriptionMarketUrl).addHeader(Const.EXCHANGE_CODE, Const.EXCHANGE_PRO_CODE)
        .addHeader(Const.CODEC, CommonConstant.CODEC_PROTOBUF).build()
        : new Request.Builder().url(subscriptionTradingUrl).addHeader(Const.EXCHANGE_CODE, Const.EXCHANGE_PRO_CODE)
            .addHeader(Const.CODEC, CommonConstant.CODEC_PROTOBUF).build();
    this.watchDog = watchDog;
    log.info("[Sub] Connection [id: "
        + this.connectionId
        + "] created for " + request.name);
  }

  void connect() {
    if (state == ConnectionState.CONNECTED) {
      log.info("[Sub][" + this.connectionId + "] Already connected");
      return;
    }
    log.info("[Sub][" + this.connectionId + "] Connecting...");
    webSocket = RestApiInvoker.createWebSocket(okhttpRequest, this);
  }

  void reConnect(int delayInSecond) {
    log.warn("[Sub][" + this.connectionId + "] Reconnecting after "
        + delayInSecond + " seconds later");
    if (webSocket != null) {
      webSocket.cancel();
      webSocket = null;
    }
    this.delayInSecond = delayInSecond;
    state = ConnectionState.DELAY_CONNECT;
  }

  void reConnect() {
    if (delayInSecond != 0) {
      delayInSecond--;
    } else {
      connect();
    }
  }

  void send(String str) {
    boolean result = false;
    if (webSocket != null) {
      result = webSocket.send(str);
    }
    if (!result) {
      log.error("[Sub][" + this.connectionId
          + "] Failed to send message");
      closeOnError();
    }
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    super.onMessage(webSocket, text);
    lastReceivedTime = TimeService.getCurrentTimeStamp();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    super.onMessage(webSocket, bytes);
    try {
      if (request == null) {
        log.error("[Sub][" + this.connectionId
            + "] request is null");
        closeOnError();
        return;
      }

      lastReceivedTime = TimeService.getCurrentTimeStamp();

      R r;
      try {
        r = EventDecoder.decode(bytes.toByteArray());
      } catch (IOException e) {
        log.error("[Sub][" + this.connectionId
            + "] Receive message error: " + e.getMessage());
        closeOnError();
        return;
      }
      if (r.action.equals(Action.PING.toString())) {
        processPingOnMarketLine(webSocket, r);
      } else if (r.action.equals(Action.PUSH.toString()) || r.action.equals(Action.REQ.toString())) {
        onReceive(r);
      }
    } catch (Exception e) {
      log.error("[Sub][" + this.connectionId
          + "] Unexpected error: " + e.getMessage());
      closeOnError();
    }
  }

  private void onError(String errorMessage, Throwable e) {
    if (request.errorHandler != null) {
      HuobiApiException exception = new HuobiApiException(
          HuobiApiException.SUBSCRIPTION_ERROR, errorMessage, e);
      request.errorHandler.onError(exception);
    }
    log.error("[Sub][" + this.connectionId + "] " + errorMessage);
  }

  @SuppressWarnings("unchecked")
  private void onReceive(R r) {
    Object obj = null;
    try {
      obj = request.parser.parse(r);
    } catch (Exception e) {
      onError("Failed to parse server's response: " + e.getMessage(), e);
    }
    try {
      request.updateCallback.onReceive(obj);
    } catch (Exception e) {
      onError("Process error: " + e.getMessage()
          + " You should capture the exception in your error handler", e);
    }
  }

  private void processPingOnMarketLine(WebSocket webSocket, R r) {
    long pingTime = ((EventDecoder.Ping) r.data).ts;
    PongMessage pongMessage = PongMessage.builder().action("pong").ts(pingTime).build();
    String jsonMessage = JSON.toJSONString(pongMessage);
    log.info("msg:" + jsonMessage);
    webSocket.send(jsonMessage);
  }

  public void close() {
    log.error("[Sub][" + this.connectionId + "] Closing normally");
    webSocket.cancel();
    webSocket = null;
    watchDog.onClosedNormally(this);
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    super.onClosed(webSocket, code, reason);
    if (state == ConnectionState.CONNECTED) {
      state = ConnectionState.IDLE;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    super.onOpen(webSocket, response);
    this.webSocket = webSocket;
    log.info("[Sub][" + this.connectionId + "] Connected to server");
    watchDog.onConnectionCreated(this);
    if (request.connectionHandler != null) {
      request.connectionHandler.handle(this);
    }
    state = ConnectionState.CONNECTED;
    lastReceivedTime = TimeService.getCurrentTimeStamp();
    if (request.authHandler != null) {
      ApiSignature as = new ApiSignature();
      UrlParamsBuilder builder = UrlParamsBuilder.build();
      try {
        URI uri = new URI(subscriptionTradingUrl);
        as.createSignature(apiKey, secretKey, "GET", tradingHost, uri.getPath(), builder);
      } catch (Exception e) {
        onError("Unexpected error when create the signature: " + e.getMessage(), e);
        close();
        return;
      }
      builder.putToUrl(ApiSignature.op, ApiSignature.opValue)
          .putToUrl("cid", TimeService.getCurrentTimeStamp());
      send(builder.buildUrlToJsonString());
      InternalUtils.await(100);
    }
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    onError("Unexpected error: " + t.getMessage(), t);
    closeOnError();
  }

  private void closeOnError() {
    if (webSocket != null) {
      this.webSocket.cancel();
      state = ConnectionState.CLOSED_ON_ERROR;
      log.error("[Sub][" + this.connectionId + "] Connection is closing due to error");
    }
  }
}
