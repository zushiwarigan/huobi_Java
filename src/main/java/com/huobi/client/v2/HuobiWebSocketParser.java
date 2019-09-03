package com.huobi.client.v2;

import okio.ByteString;

import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public interface HuobiWebSocketParser<T> {

  T parse(ByteString byteString) throws Exception;

  T parse(Result result) throws Exception;

}
