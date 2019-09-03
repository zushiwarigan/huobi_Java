package com.huobi.client.v2.impl;

import java.util.LinkedList;
import java.util.List;

import com.huobi.client.v2.HuobiMarketWebSocketClient;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketOptions;
import com.huobi.client.v2.enums.CandlestickIntervalEnum;
import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;
import com.huobi.client.v2.message.AggrTradesMessage;
import com.huobi.client.v2.message.CandlestickMessage;
import com.huobi.client.v2.message.PriceDepthMessage;
import com.huobi.client.v2.message.TradeMessage;
import com.huobi.client.v2.message.TradeOverviewMessage;
import com.huobi.client.v2.message.TradeSummaryMessage;

public class HuobiMarketWebSocketClientImpl implements HuobiMarketWebSocketClient {


  private HuobiWebSocketRequestImpl requestImpl;

  private HuobiWebSocketOptions options;

  private HuobiWebSocketWatchDog watchDog;

  private final List<HuobiWebSocketConnection> connections = new LinkedList<>();

  public HuobiMarketWebSocketClientImpl(HuobiWebSocketOptions options) {
    this.options = options;
    this.requestImpl = new HuobiWebSocketRequestImpl();
  }

  public void createConnection(HuobiWebSocketRequest request) {
    if (watchDog == null) {
      watchDog = new HuobiWebSocketWatchDog(options);
    }
    HuobiWebSocketConnection connection = new HuobiWebSocketConnection(options, request, watchDog);

    connections.add(connection);
    connection.connect();
  }

  @Override
  public void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback<CandlestickMessage> callback) {
    createConnection(requestImpl.createCandlestickEvent(symbols, interval, callback));
  }

  @Override
  public void requestCandlestickEvent(String symbols, CandlestickIntervalEnum interval, Long from, Long to,
      HuobiWebSocketCallback<CandlestickMessage> callback) {
    createConnection(requestImpl.createReqCandlestickEvent(symbols, from, to, interval, callback));
  }

  @Override
  public void subscribePriceDepthEvent(String symbols, HuobiWebSocketCallback<PriceDepthMessage> callback) {
    subscribePriceDepthEvent(symbols, DepthLevelEnum.LEVEL_5, DepthStepEnum.STEP0, callback);
  }

  @Override
  public void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback) {
    createConnection(requestImpl.createPriceDepthEvent(symbols, depthLevel, depthStep, callback));
  }

  public void requestPriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback) {
    createConnection(requestImpl.createReqPriceDepthEvent(symbols, depthLevel, depthStep, callback));
  }

  @Override
  public void subscribeTradeEvent(String symbols, HuobiWebSocketCallback<TradeMessage> callback) {
    createConnection(requestImpl.createTradeEvent(symbols, callback));
  }

  @Override
  public void requestTradeEvent(String symbols, int limit, HuobiWebSocketCallback<TradeMessage> callback) {
    createConnection(requestImpl.createReqTradeEvent(symbols, limit, callback));
  }

  @Override
  public void subscribeAggrTradesEvent(String symbols, HuobiWebSocketCallback<AggrTradesMessage> callback) {
    createConnection(requestImpl.createAggrTradesEvent(symbols, callback));
  }

  @Override
  public void subscribeTradeOverviewEvent(HuobiWebSocketCallback<TradeOverviewMessage> callback) {
    createConnection(requestImpl.createTradeOverviewEvent(callback));
  }

  @Override
  public void subscribeTradeSummaryEvent(String symbols, HuobiWebSocketCallback<TradeSummaryMessage> callback) {
    createConnection(requestImpl.createTradeSummaryEvent(symbols, callback));
  }

  @Override
  public void requestTradeSummaryEvent(String symbols, HuobiWebSocketCallback<TradeSummaryMessage> callback) {
    createConnection(requestImpl.createReqTradeSummaryEvent(symbols, callback));
  }


}
