package com.huobi.client.impl;

import com.huobi.gateway.EventDecoder.R;

@FunctionalInterface
interface RestApiParser<T> {

  T parse(R r);
}
