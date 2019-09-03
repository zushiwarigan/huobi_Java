package com.huobi.client.v2.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;

import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.client.v2.enums.SideEnum;
import com.huobi.client.v2.message.TradeMessage;
import com.huobi.client.v2.message.entry.TradeEntry;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class TradeMessageParser implements HuobiWebSocketParser<TradeMessage> {

  @Override
  public TradeMessage parse(ByteString byteString) throws Exception {
    Result result = Result.parseFrom(byteString.toByteArray());
    return parse(result);
  }

  @Override
  public TradeMessage parse(Result result) throws Exception {
    TradeMessage message;
    if (result.getAction() == Action.REQ) {
      MarketDownstreamProtocol.ReqTrade data = result.getData().unpack(MarketDownstreamProtocol.ReqTrade.class);
      List<TradeEntry> list = new ArrayList<>();


      data.getTradesList().forEach(tick -> {
        list.add(TradeEntry.builder()
            .tradeId(tick.getTradeId())
            .price(new BigDecimal(tick.getPrice()))
            .volume(new BigDecimal(tick.getVolume()))
            .side(SideEnum.getByCode(tick.getSideValue()))
            .timestamp(tick.getTs())
            .build());
      });

      message = TradeMessage.builder()
          .symbol(data.getSymbol())
          .tradeList(list)
          .build();
    } else {
      MarketDownstreamProtocol.Trade data = result.getData().unpack(MarketDownstreamProtocol.Trade.class);
      TradeEntry entry = TradeEntry.builder()
          .tradeId(data.getTradeId())
          .price(new BigDecimal(data.getPrice()))
          .volume(new BigDecimal(data.getVolume()))
          .side(SideEnum.getByCode(data.getSideValue()))
          .timestamp(data.getTs())
          .build();
      List<TradeEntry> list = new ArrayList<TradeEntry>(1);
      list.add(entry);
      message = TradeMessage.builder()
          .symbol(data.getSymbol())
          .tradeList(list)
          .build();
    }

    return message;
  }
}
