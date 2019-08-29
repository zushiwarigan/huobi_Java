package com.huobi.client.v1.impl;

import com.huobi.client.v1.SubscriptionErrorHandler;
import com.huobi.client.v1.SubscriptionListener;
import com.huobi.client.v1.utils.Handler;

class WebsocketRequest<T> {

  WebsocketRequest(SubscriptionListener<T> listener, SubscriptionErrorHandler errorHandler) {
    this.updateCallback = listener;
    this.errorHandler = errorHandler;
  }

  String name;
  Handler<WebSocketConnection> connectionHandler;
  Handler<WebSocketConnection> authHandler = null;
  final SubscriptionListener<T> updateCallback;
  RestApiJsonParser<T> jsonParser;
  final SubscriptionErrorHandler errorHandler;
}
