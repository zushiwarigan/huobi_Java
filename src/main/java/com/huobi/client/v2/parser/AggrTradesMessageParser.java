package com.huobi.client.v2.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;

import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.client.v2.enums.SideEnum;
import com.huobi.client.v2.message.AggrTradesMessage;
import com.huobi.client.v2.message.entry.AggrTradeEntry;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class AggrTradesMessageParser implements HuobiWebSocketParser<AggrTradesMessage> {

  @Override
  public AggrTradesMessage parse(ByteString byteString) throws Exception {
    Result result = Result.parseFrom(byteString.toByteArray());
    return parse(result);
  }

  @Override
  public AggrTradesMessage parse(Result result) throws Exception {

    MarketDownstreamProtocol.AggrTrade data = result.getData().unpack(MarketDownstreamProtocol.AggrTrade.class);

    AggrTradeEntry entry = AggrTradeEntry.builder()
        .firstTradeId(data.getFirstTradeId())
        .lastTradeId(data.getLastTradeId())
        .symbol(data.getSymbol())
        .price(new BigDecimal(data.getPrice()))
        .volume(new BigDecimal(data.getVolume()))
        .side(SideEnum.getByCode(data.getSideValue()))
        .timestamp(data.getTs())
        .build();
    List<AggrTradeEntry> list = new ArrayList<>(1);
    list.add(entry);
    return AggrTradesMessage.builder()
        .symbol(data.getSymbol())
        .timestamp(data.getTs())
        .aggrTradeList(list)
        .build();
  }
}
