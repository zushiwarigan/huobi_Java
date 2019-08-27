package com.huobi.client.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.huobi.client.SubscribeClient;
import com.huobi.client.SubscribeOption;
import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.message.AggrTradesMessage;
import com.huobi.client.message.CandlestickMessage;
import com.huobi.client.message.PriceDepthMessage;
import com.huobi.client.message.TradeMessage;
import com.huobi.client.message.TradeOverviewMessage;
import com.huobi.client.message.TradeSummaryMessage;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthLevelEnum;
import com.huobi.gateway.enums.DepthStepEnum;

public class WSStreamClientImpl implements SubscribeClient {

  private final SubscribeOption options;

  private WSWatchDog watchDog;

  private final WSRequestImpl requestImpl;


  private final List<WSConnection> connections = new LinkedList<>();

  private final String apiKey;

  private final String secretKey;

  public WSStreamClientImpl(String apiKey, String secretKey, SubscribeOption options) {
    this.apiKey = apiKey;
    this.secretKey = secretKey;
    this.watchDog = null;
    this.options = Objects.requireNonNull(options);

    this.requestImpl = new WSRequestImpl(apiKey);
  }

  private <T> void createConnection(WSRequest<T> request) {
    if (watchDog == null) {
      watchDog = new WSWatchDog(options);
    }
    WSConnection connection = new WSConnection(
        apiKey, secretKey, options, request, watchDog);
    connections.add(connection);
    connection.connect();
  }

  private List<String> parseSymbols(String symbol) {
    return Arrays.asList(symbol.split("[,]"));
  }

  @Override
  public void subscribeCandlestickEvent(
      String symbols,
      CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback) {
    subscribeCandlestickEvent(symbols, interval, callback, null);
  }

  @Override
  public void subscribeCandlestickEvent(
      String symbols,
      CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeCandlestick(
        parseSymbols(symbols), interval, subscriptionListener, errorHandler));
  }

  @Override
  public void subscribePriceDepthEvent(
      String symbols,
      SubscriptionListener<PriceDepthMessage> subscriptionListener) {
    subscribePriceDepthEvent(symbols, DepthLevelEnum.LEVEL_5, DepthStepEnum.STEP0, subscriptionListener, null);
  }

  @Override
  public void subscribePriceDepthEvent(
      String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> subscriptionListener) {
    subscribePriceDepthEvent(symbols, depthLevel, depthStep, subscriptionListener, null);
  }

  @Override
  public void subscribePriceDepthEvent(
      String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribePriceDepth(
        parseSymbols(symbols), depthLevel, depthStep, subscriptionListener, errorHandler, false));
  }

  @Override
  public void subscribeTradeEvent(String symbols, SubscriptionListener<TradeMessage> callback) {
    subscribeTradeEvent(symbols, callback, null);
  }

  @Override
  public void subscribeTradeEvent(String symbols, SubscriptionListener<TradeMessage> callback, SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeTrade(parseSymbols(symbols), callback, errorHandler));
  }

  @Override
  public void subscribeAggrTradesEvent(String symbols, SubscriptionListener<AggrTradesMessage> callback) {
    subscribeAggrTradesEvent(symbols, callback, null);
  }

  @Override
  public void subscribeAggrTradesEvent(String symbols, SubscriptionListener<AggrTradesMessage> callback, SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeAggrTrades(parseSymbols(symbols), callback, errorHandler));
  }

  @Override
  public void subscribeTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback) {
    subscribeTradeSummaryEvent(symbols, callback, null);
  }

  @Override
  public void subscribeTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback, SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeTradeSummary(parseSymbols(symbols), callback, errorHandler,false));
  }

  @Override
  public void subscribeTradeOverviewEvent(SubscriptionListener<TradeOverviewMessage> callback) {
    subscribeTradeOverviewEvent(callback, null);
  }

  @Override
  public void subscribeTradeOverviewEvent(SubscriptionListener<TradeOverviewMessage> callback,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeTradeOverview(callback, errorHandler));
  }

  @Override
  public void requestCandlestickEvent(String symbols, Long from, Long to, CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback) {
    requestCandlestickEvent(symbols, from, to, interval, callback, null);
  }

  @Override
  public void requestCandlestickEvent(String symbols, Long from, Long to, CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback,
      SubscriptionErrorHandler errorHandler) {

    createConnection(requestImpl.requestCandlestickEvent(parseSymbols(symbols), from, to, interval, callback, errorHandler));
  }

  @Override
  public void requestPriceDepth(String symbols, SubscriptionListener<PriceDepthMessage> callback) {
    requestPriceDepth(symbols, DepthLevelEnum.LEVEL_5, DepthStepEnum.STEP0, callback, null);
  }

  @Override
  public void requestPriceDepth(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> callback) {
    requestPriceDepth(symbols, depthLevel, depthStep, callback, null);
  }

  @Override
  public void requestPriceDepth(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> callback,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribePriceDepth(
        parseSymbols(symbols), depthLevel, depthStep, callback, errorHandler, false));
  }

  @Override
  public void requestTradeEvent(String symbols, int limit, SubscriptionListener<TradeMessage> callback) {
    requestTradeEvent(symbols, limit, callback, null);
  }

  @Override
  public void requestTradeEvent(String symbols, int limit, SubscriptionListener<TradeMessage> callback, SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.requestTrade(parseSymbols(symbols), limit, callback, errorHandler));
  }

  @Override
  public void requestTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback) {
    requestTradeSummaryEvent(symbols,callback,null);
  }

  @Override
  public void requestTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback, SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeTradeSummary(parseSymbols(symbols), callback, errorHandler,true));
  }


  @Override
  public void unsubscribeAll() {
    for (WSConnection connection : connections) {
      watchDog.onClosedNormally(connection);
      connection.close();
    }
    connections.clear();
  }
}

