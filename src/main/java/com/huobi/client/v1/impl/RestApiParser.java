package com.huobi.client.v1.impl;

import com.huobi.gateway.EventDecoder.R;

@FunctionalInterface
interface RestApiParser<T> {

  T parse(R r);
}
