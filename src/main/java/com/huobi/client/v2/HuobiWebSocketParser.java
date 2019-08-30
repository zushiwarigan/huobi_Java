package com.huobi.client.v2;

import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public interface HuobiWebSocketParser<T> {

  T parse(Result result) throws Exception;

}
