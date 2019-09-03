package com.huobi.client.v2.impl;

import java.util.Arrays;
import java.util.List;

import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.constant.ChannelBuilder;
import com.huobi.client.v2.enums.CandlestickIntervalEnum;
import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;
import com.huobi.client.v2.message.CandlestickMessage;
import com.huobi.client.v2.parser.AggrTradesMessageParser;
import com.huobi.client.v2.parser.CandlestickMessageParser;
import com.huobi.client.v2.parser.PriceDepthMessageParser;
import com.huobi.client.v2.parser.TradeMessageParser;
import com.huobi.client.v2.parser.TradeOverviewMessageParser;
import com.huobi.client.v2.parser.TradeSummaryMessageParser;

public class HuobiWebSocketRequestImpl {


  public HuobiWebSocketRequest<CandlestickMessage> createCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildCandlestickChannel(symbol, interval);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new CandlestickMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createReqCandlestickEvent(String symbols, Long from, Long to, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildCandlestickReqChannel(symbol, from, to, interval);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new CandlestickMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createPriceDepthEvent(String symbols,
      DepthLevelEnum levelEnum,
      DepthStepEnum stepEnum,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildPriceDepthChannel(symbol, levelEnum, stepEnum);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new PriceDepthMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createReqPriceDepthEvent(String symbols,
      DepthLevelEnum levelEnum,
      DepthStepEnum stepEnum,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildPriceDepthReqChannel(symbol, levelEnum, stepEnum);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new PriceDepthMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createTradeEvent(String symbols,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildTradeChannel(symbol);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new TradeMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createReqTradeEvent(String symbols, int limit,
      HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildTradeReqChannel(symbol, limit);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new TradeMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createAggrTradesEvent(String symbols, HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildAggrTradesChannel(symbol);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new AggrTradesMessageParser())
        .build();
  }


  public HuobiWebSocketRequest<CandlestickMessage> createTradeOverviewEvent(HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          String req = ChannelBuilder.buildTradeOverviewChannel();
          connection.send(req);
        })
        .callback(callback)
        .parser(new TradeOverviewMessageParser())
        .build();
  }



  public HuobiWebSocketRequest<CandlestickMessage> createTradeSummaryEvent(String symbols, HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildTradeSummaryChannel(symbol);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new TradeSummaryMessageParser())
        .build();
  }

  public HuobiWebSocketRequest<CandlestickMessage> createReqTradeSummaryEvent(String symbols, HuobiWebSocketCallback callback) {
    return HuobiWebSocketRequest.builder()
        .connectionHandler(connection -> {
          parseSymbols(symbols).forEach(symbol -> {
            String req = ChannelBuilder.buildTradeReqSummaryChannel(symbol);
            connection.send(req);
          });
        })
        .callback(callback)
        .parser(new TradeSummaryMessageParser())
        .build();
  }

  private List<String> parseSymbols(String symbol) {
    return Arrays.asList(symbol.split("[,]"));
  }

}
