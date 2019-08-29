package com.huobi.client.v1.impl;

import com.huobi.client.v1.utils.JsonWrapper;

@FunctionalInterface
interface RestApiJsonParser<T> {

  T parseJson(JsonWrapper json);
}
