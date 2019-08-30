package com.huobi.client.v2.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.client.v1.message.model.PriceLevelEntry;
import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class PriceDepthMessageParser implements HuobiWebSocketParser<PriceDepthMessage> {

  @Override
  public PriceDepthMessage parse(Result result) throws Exception {
    MarketDownstreamProtocol.Depth data = result.getData().unpack(MarketDownstreamProtocol.Depth.class);

    List<PriceLevelEntry> bids = new ArrayList<>(data.getBidsCount());
    List<PriceLevelEntry> asks = new ArrayList<>(data.getAsksCount());
    data.getBidsList().forEach(bid ->{
      bids.add(PriceLevelEntry.builder()
          .price(new BigDecimal(bid.getPrice()))
          .size(new BigDecimal(bid.getSize()))
          .build());
    });


    data.getAsksList().forEach(ask ->{
      asks.add(PriceLevelEntry.builder()
          .price(new BigDecimal(ask.getPrice()))
          .size(new BigDecimal(ask.getSize()))
          .build());
    });

    return PriceDepthMessage.builder()
        .symbol(data.getSymbol())
        .timestamp(data.getTs())
        .bids(bids)
        .asks(asks)
        .build();
  }
}
