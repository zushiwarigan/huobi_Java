package com.huobi.client.v2.impl;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.client.v1.utils.ChannelUtil;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketClient;
import com.huobi.client.v2.HuobiWebSocketOptions;
import com.huobi.client.v2.WebSocketWatchDog;
import com.huobi.client.v2.enums.ChannelEnums;
import com.huobi.client.v2.parser.CandlestickMessageParser;
import com.huobi.client.v2.parser.PriceDepthMessageParser;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthLevelEnum;
import com.huobi.gateway.enums.DepthStepEnum;

@Slf4j
public class HuobiWebSocketClientImpl implements HuobiWebSocketClient {

  private HuobiWebSocketListener listener;

  private WebSocketWatchDog watchDog;

  private HuobiWebSocketConnection connection;

  public HuobiWebSocketClientImpl(HuobiWebSocketOptions options) {
    listener = new HuobiWebSocketListener();
    connection = new HuobiWebSocketConnection(options, listener);
    watchDog = new WebSocketWatchDog(connection);
  }


  @Override
  public void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval, HuobiWebSocketCallback<CandlestickMessage> callback) {

    listener.addParser(ChannelEnums.CANDLESTICK, new CandlestickMessageParser());

    parseSymbols(symbols).forEach(symbol -> {
      String req = ChannelUtil.candlestickChannel(symbol, interval);
      String topic = ChannelUtil.getCandlestickChannel(symbol, interval);
      listener.addCallback(topic, callback);
      connection.send(req);
    });
  }

  @Override
  public void requestCandlestickEvent(String symbols, Long from, Long to, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback<CandlestickMessage> callback) {

    listener.addParser(ChannelEnums.CANDLESTICK, new CandlestickMessageParser());

    parseSymbols(symbols).forEach(symbol -> {
      String req = ChannelUtil.candlestickReqChannel(symbol, from, to, interval);
      String topic = ChannelUtil.getCandlestickChannel(symbol, interval);
      listener.addCallback(topic, callback);
      connection.send(req, false);
    });
  }

  @Override
  public void subscribePriceDepthEvent(String symbols, HuobiWebSocketCallback<PriceDepthMessage> callback) {
    subscribePriceDepthEvent(symbols, DepthLevelEnum.LEVEL_5, DepthStepEnum.STEP0, callback);
  }

  @Override
  public void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback) {
    listener.addParser(ChannelEnums.PRICE_DEPTH , new PriceDepthMessageParser());

    parseSymbols(symbols).forEach(symbol -> {
      String req = ChannelUtil.priceDepthChannel(symbol,depthLevel.level, depthStep);
      String topic = ChannelUtil.getPriceDepthChannel(symbol,depthLevel.level, depthStep);
      listener.addCallback(topic, callback);
      connection.send(req);
    });
  }


  private List<String> parseSymbols(String symbol) {
    return Arrays.asList(symbol.split("[,]"));
  }
}
