package com.huobi.client.v2.parser;

import java.math.BigDecimal;

import okio.ByteString;

import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.client.v2.message.TradeSummaryMessage;
import com.huobi.client.v2.message.entry.TradeSummaryEntry;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class TradeSummaryMessageParser implements HuobiWebSocketParser<TradeSummaryMessage> {

  @Override
  public TradeSummaryMessage parse(ByteString byteString) throws Exception {
    Result result = Result.parseFrom(byteString.toByteArray());
    return parse(result);
  }

  @Override
  public TradeSummaryMessage parse(Result result) throws Exception {

    MarketDownstreamProtocol.MarketSummary data = result.getData().unpack(MarketDownstreamProtocol.MarketSummary.class);
    return TradeSummaryMessage.builder()
        .symbol(data.getSymbol())
        .timestamp(data.getTs())
        .tradeSummary(TradeSummaryEntry.builder()
            .symbol(data.getSymbol())
            .timestamp(data.getTs())
            .open(new BigDecimal(data.getOpen()))
            .close(new BigDecimal(data.getClose()))
            .high(new BigDecimal(data.getHigh()))
            .low(new BigDecimal(data.getLow()))
            .turnover(new BigDecimal(data.getTurnover()))
            .volume(new BigDecimal(data.getVolume()))
            .numOfTrades(data.getNumOfTrades())
            .build())
        .build();
  }
}
