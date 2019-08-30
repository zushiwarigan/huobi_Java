package com.huobi.client.v2.impl;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.WebSocket;

import com.huobi.client.v1.base.CommonConstant;
import com.huobi.client.v2.HuobiWebSocketOptions;
import com.huobi.gateway.constant.Const;

@Slf4j
public class HuobiWebSocketConnection {

  @Getter
  private OkHttpClient client;

  @Getter
  private WebSocket webSocket;

  @Getter
  private Request okHttpRequest;

  @Getter
  private HuobiWebSocketListener listener;

  @Getter
  HuobiWebSocketOptions options;

  private List<String> subscribeTopicList = new LinkedList<>();

  public HuobiWebSocketConnection(HuobiWebSocketOptions options ,HuobiWebSocketListener listener){
    this.listener = listener;
    this.options = options;
    client = new OkHttpClient();

    okHttpRequest = new Builder()
        .url(options.getUri())
        .addHeader(Const.CODEC, CommonConstant.CODEC_PROTOBUF)
        .addHeader(Const.EXCHANGE_CODE, Const.EXCHANGE_PRO_CODE)
        .build();

    webSocket = client.newWebSocket(okHttpRequest, listener);
  }


  public void reConnect() {
    log.info("[WebSocket] reConnect...");
    if (webSocket != null) {
      webSocket.cancel();
      webSocket = null;
    }
    webSocket = client.newWebSocket(okHttpRequest, listener);
    if (!subscribeTopicList.isEmpty()) {
      subscribeTopicList.forEach(topic -> {
        send(topic, false);
      });
    }
  }

  public void send(String topic) {
    send(topic, true);
  }

  public void send(String topic, boolean storage) {
    webSocket.send(topic);
    if (storage) {
      log.info("[WebSocket] send : {}", topic);
      subscribeTopicList.add(topic);
    }
  }


}
