package com.huobi.client.v2.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;

import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.client.v2.message.TradeOverviewMessage;
import com.huobi.client.v2.message.entry.TradeOverviewEntry;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Overview.Tick;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class TradeOverviewMessageParser implements HuobiWebSocketParser<TradeOverviewMessage> {

  @Override
  public TradeOverviewMessage parse(ByteString byteString) throws Exception {
    Result result = Result.parseFrom(byteString.toByteArray());
    return parse(result);
  }

  @Override
  public TradeOverviewMessage parse(Result result) throws Exception {

    MarketDownstreamProtocol.Overview data = result.getData().unpack(MarketDownstreamProtocol.Overview.class);

    List<Tick> tickList = data.getTickList();
    List<TradeOverviewEntry> list = new ArrayList<>(tickList.size());
    tickList.forEach(tick -> {
      list.add(TradeOverviewEntry.builder()
          .symbol(tick.getSymbol())
          .open(new BigDecimal(tick.getOpen()))
          .close(new BigDecimal(tick.getClose()))
          .high(new BigDecimal(tick.getHigh()))
          .low(new BigDecimal(tick.getLow()))
          .turnover(new BigDecimal(tick.getTurnover()))
          .volume(new BigDecimal(tick.getVolume()))
          .numOfTrades(tick.getNumOfTrades())
          .build());
    });

    return TradeOverviewMessage.builder()
        .timestamp(data.getTs())
        .overviewList(list)
        .build();
  }
}
