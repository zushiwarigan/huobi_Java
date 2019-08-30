package com.huobi.client.v2.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import com.huobi.client.v1.message.PongMessage;
import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.client.v2.enums.ChannelEnums;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Ping;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

@Slf4j
public class HuobiWebSocketListener<T> extends WebSocketListener {

  Map<String, HuobiWebSocketCallback<T>> callbackMap = new LinkedHashMap<>();
  Map<String, HuobiWebSocketParser<T>> parserMap = new LinkedHashMap<>();

  private int state = ConnectState.NORMAL;


  @Override
  public void onMessage(WebSocket webSocket, String text) {
    log.info("[On Message]:{}" + text);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString byteString) {
    Result result;
    try {

      result = Result.parseFrom(byteString.toByteArray());
    } catch (Exception e) {
      log.info("[On Message] Unexpected message", e);
      return;
    }

    if (result.getAction().equals(Action.PING)) {
      pong(webSocket, result);
    } else if (result.getAction().equals(Action.PUSH) || result.getAction().equals(Action.REQ)) {
      handleMessage(result);
    }
  }

  public void handleMessage(Result result) {

    String ch = result.getCh();
    HuobiWebSocketCallback callback = callbackMap.get(ch);
    if (callback == null) {
      log.info("[Handle Message] no callback: {}", result);
      return;
    }
    HuobiWebSocketParser parser = null;
    ChannelEnums parserChannel = null;
    if (ch.startsWith(ChannelEnums.CANDLESTICK.getChannelName())) {
      parserChannel = ChannelEnums.CANDLESTICK;
    } else if (ch.startsWith(ChannelEnums.PRICE_DEPTH.getChannelName())) {
      parserChannel = ChannelEnums.PRICE_DEPTH;
    }

    parser = parserMap.get(parserChannel.getChannelName());
    if (parser == null) {
      log.error("[Handle Message] no parser to parse message:{}", result);
      return;
    }
    Object object;
    try {
      object = parser.parse(result);
      callback.onResponse(object);
    } catch (Exception e) {
      log.error("[Handle Message] parse message fail: {}", result, e);
      return;
    }

  }

  @Override
  public void onClosing(final WebSocket webSocket, final int code, final String reason) {

  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    state = ConnectState.FAILURE;
    log.error("[onFailure]", t);
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    state = ConnectState.NORMAL;
    log.info("[onOpen] {}", response.toString());
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    log.error("[onClosed] code: {} reason:{}", code, reason);
  }

  public void addCallback(String topic, HuobiWebSocketCallback callback) {
    callbackMap.putIfAbsent(topic, callback);
  }

  public void addParser(ChannelEnums channel, HuobiWebSocketParser parser) {
    parserMap.putIfAbsent(channel.getChannelName(), parser);
  }

  public void pong(WebSocket webSocket, Result result) {
    try {
      Ping ping = result.getData().unpack(Ping.class);
      PongMessage pongMessage = PongMessage.builder().action("pong").ts(ping.getTs()).build();
      String jsonMessage = JSON.toJSONString(pongMessage);
      webSocket.send(jsonMessage);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public int getState() {
    return state;
  }

  public static class ConnectState {
    public static final int NORMAL = 1;
    public static final int FAILURE = 2;
  }

}


