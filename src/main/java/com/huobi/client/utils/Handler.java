package com.huobi.client.utils;

@FunctionalInterface
public interface Handler<T> {

  void handle(T t);
}
