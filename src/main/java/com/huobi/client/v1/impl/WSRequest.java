package com.huobi.client.v1.impl;

import com.huobi.client.v1.SubscriptionErrorHandler;
import com.huobi.client.v1.SubscriptionListener;
import com.huobi.client.v1.utils.Handler;

public class WSRequest<T> {
  String name;
  Handler<WSConnection> connectionHandler;
  Handler<WSConnection> authHandler = null;
  final SubscriptionListener<T> updateCallback;
  RestApiParser<T> parser;
  final SubscriptionErrorHandler errorHandler;

  public WSRequest(SubscriptionListener<T> listener, SubscriptionErrorHandler errorHandler) {
    this.updateCallback = listener;
    this.errorHandler = errorHandler;
  }
}
