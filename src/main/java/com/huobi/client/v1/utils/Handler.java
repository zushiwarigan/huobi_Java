package com.huobi.client.v1.utils;

@FunctionalInterface
public interface Handler<T> {

  void handle(T t);
}
