package com.huobi.client.v2;

@FunctionalInterface
public interface HuobiWebSocketCallback<T> {

  void onResponse(T response);

  default void onFailure(Throwable cause) {}
}
