package com.huobi.client.v2.impl;

import com.google.protobuf.InvalidProtocolBufferException;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import com.huobi.client.impl.utils.InternalUtils;
import com.huobi.client.impl.utils.TimeService;
import com.huobi.client.v2.HuobiWebSocketOptions;
import com.huobi.client.v2.constant.Const;
import com.huobi.client.v2.message.PongMessage;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Ping;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

@Slf4j
public class HuobiWebSocketConnection  extends WebSocketListener {

  public enum ConnectionState {
    IDLE,
    DELAY_CONNECT,
    CONNECTED,
    CLOSED_ON_ERROR,
    CLOSE,
  }

  private int delayInSecond = 0;

  private WebSocket webSocket;

  private Request okHttpRequest;

  private HuobiWebSocketWatchDog watchDog;

  @Getter
  private volatile long lastReceivedTime = 0;

  @Getter
  @Setter
  private volatile ConnectionState state = ConnectionState.IDLE;

  private final HuobiWebSocketRequest request;


  public HuobiWebSocketConnection(HuobiWebSocketOptions options, HuobiWebSocketRequest request,HuobiWebSocketWatchDog watchDog) {
    this.watchDog = watchDog;
    this.request = request;
    this.okHttpRequest = new Builder()
        .url(options.getUri())
        .addHeader(Const.CODEC, Const.CODEC_PROTOBUF)
        .addHeader(Const.EXCHANGE_CODE, Const.EXCHANGE_PRO_CODE)
        .build();

//    webSocket = ClientFactory.createWebSocket(okHttpRequest, this);

  }

  public void send(String s) {
    System.out.println("send ==>"+s);
    webSocket.send(s);
  }


  void connect() {
    if (getState() == ConnectionState.CONNECTED) {
      log.info("[HuobiWebSocketConnection] Already connected");
      return;
    }
    log.info("[HuobiWebSocketConnection] Connecting...");
    webSocket = ClientFactory.createWebSocket(okHttpRequest, this);
  }

  void reConnect(int delayInSecond) {
    log.warn("[HuobiWebSocketConnection] Reconnecting after {} seconds later", delayInSecond);
    if (webSocket != null) {
      webSocket.cancel();
      webSocket = null;
    }
    this.delayInSecond = delayInSecond;
    setState(ConnectionState.DELAY_CONNECT);
  }

  void reConnect() {
    if (delayInSecond != 0) {
      delayInSecond--;
    } else {
      connect();
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

      lastReceivedTime = TimeService.getCurrentTimeStamp();

      Result data;
      try {
        data = Result.parseFrom(bytes.toByteArray());
      } catch (Exception e) {
        log.error("[HuobiWebSocketConnection] Parse Data Exception: ", e);
        closeOnError();
        return;
      }

      if (data.getAction() == Action.PING) {
        pong(webSocket, data);
      } else if (data.getAction() == Action.PUSH || data.getAction() == Action.REQ) {
        onReceive(data);
      }


    } catch (Exception e) {
      log.error("[HuobiWebSocketConnection] Unexpected error: ", e);
      closeOnError();
    }
  }

  private void onError(String errorMessage, Throwable e) {
    log.error("[HuobiWebSocketConnection] ErrorMessage:{}", errorMessage, e);
  }

  private void onReceive(Result data) {
    Object obj = null;
    try {
      obj = request.getParser().parse(data);
    } catch (Exception e) {
      onError("Process error: " + e.getMessage()
          + " You should capture the exception in your error handler", e);
    }

    try {
      request.getCallback().onReceive(obj);
    } catch (Exception e) {
      onError("Callback error:" + e.getMessage(), e);
    }
  }

  private void pong(WebSocket webSocket, Result result) {
    try {
      Ping ping = result.getData().unpack(Ping.class);
      PongMessage pongMessage = PongMessage.builder().action("pong").ts(ping.getTs()).build();
      String jsonMessage = JSON.toJSONString(pongMessage);
      webSocket.send(jsonMessage);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }


  public ConnectionState getState() {
    return state;
  }

  public void close() {
    log.error("[HuobiWebSocketConnection] Closing normally");
    webSocket.cancel();
    webSocket = null;
    state = ConnectionState.CLOSE;
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
    state = ConnectionState.CONNECTED;
    watchDog.onConnectionCreated(this);
    log.info("[HuobiWebSocketConnection] Connected to server");

    if (request.getConnectionHandler() != null) {
      request.getConnectionHandler().handle(this);
    }

    lastReceivedTime = TimeService.getCurrentTimeStamp();

    if (request.getAuthHandler() != null) {
      request.getAuthHandler().handle(this);
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
      log.error("[HuobiWebSocketConnection] Connection is closing due to error");
    }
  }


}
