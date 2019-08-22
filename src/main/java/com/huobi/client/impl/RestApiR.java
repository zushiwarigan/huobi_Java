package com.huobi.client.impl;

import com.huobi.gateway.EventDecoder.R;

@FunctionalInterface
interface RestApiR<T> {

  T parseR(R r);
}
