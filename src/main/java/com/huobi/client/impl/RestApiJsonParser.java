package com.huobi.client.impl;

import com.huobi.client.utils.JsonWrapper;

@FunctionalInterface
interface RestApiJsonParser<T> {

  T parseJson(JsonWrapper json);
}
