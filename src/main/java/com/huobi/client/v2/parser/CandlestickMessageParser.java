package com.huobi.client.v2.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v1.message.model.CandlestickEntry;
import com.huobi.client.v2.HuobiWebSocketParser;
import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.ReqCandlestick;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class CandlestickMessageParser implements HuobiWebSocketParser<CandlestickMessage> {

  @Override
  public CandlestickMessage parse(Result result) throws Exception {
    CandlestickMessage message = new CandlestickMessage();
    if (result.getAction().equals(Action.PUSH)) {
      MarketDownstreamProtocol.Candlestick data = result.getData().unpack(MarketDownstreamProtocol.Candlestick.class);
      message.setSymbol(data.getSymbol());

      CandlestickEntry entry = CandlestickEntry.builder()
          .id(data.getId())
          .timestamp(data.getTs())
          .open(new BigDecimal(data.getOpen()))
          .close(new BigDecimal(data.getClose()))
          .high(new BigDecimal(data.getHigh()))
          .low(new BigDecimal(data.getLow()))
          .volume(new BigDecimal(data.getVolume()))
          .turnover(new BigDecimal(data.getTurnover()))
          .numOfTrades(data.getNumOfTrades())
          .build();
      List<CandlestickEntry> dataList = new ArrayList<>(1);
      dataList.add(entry);
      message.setDataList(dataList);
    } else {
      MarketDownstreamProtocol.ReqCandlestick data = result.getData().unpack(MarketDownstreamProtocol.ReqCandlestick.class);
      message.setSymbol(data.getSymbol());

      List<ReqCandlestick.Tick> tickList = data.getCandlesticksList();
      List<CandlestickEntry> dataList = new ArrayList<>(tickList.size());
      tickList.forEach(tick -> {
        CandlestickEntry entry = CandlestickEntry.builder()
            .id(tick.getId())
            .timestamp(tick.getTs())
            .open(new BigDecimal(tick.getOpen()))
            .close(new BigDecimal(tick.getClose()))
            .high(new BigDecimal(tick.getHigh()))
            .low(new BigDecimal(tick.getLow()))
            .volume(new BigDecimal(tick.getVolume()))
            .turnover(new BigDecimal(tick.getTurnover()))
            .numOfTrades(tick.getNumOfTrades())
            .build();
        dataList.add(entry);
      });
      message.setDataList(dataList);
    }
    return message;
  }
}
