package com.huobi.client.v2.impl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

class ClientFactory {

  private static final OkHttpClient CLIENT = new OkHttpClient();

  static WebSocket createWebSocket(Request request, WebSocketListener listener) {
    return CLIENT.newWebSocket(request, listener);
  }

}
