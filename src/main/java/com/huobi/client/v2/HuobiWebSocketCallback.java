package com.huobi.client.v2;

public interface HuobiWebSocketCallback<T> {

  void onReceive(T t);

}
