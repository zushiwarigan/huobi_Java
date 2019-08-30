package com.huobi.client.v1.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.huobi.client.v1.SubscriptionClient;
import com.huobi.client.v1.SubscriptionErrorHandler;
import com.huobi.client.v1.SubscriptionListener;
import com.huobi.client.v1.SubscriptionOptions;
import com.huobi.client.v1.enums.BalanceMode;
import com.huobi.client.v1.enums.CandlestickInterval;
import com.huobi.client.v1.event.AccountEvent;
import com.huobi.client.v1.event.CandlestickEvent;
import com.huobi.client.v1.event.OrderUpdateEvent;
import com.huobi.client.v1.event.PriceDepthEvent;
import com.huobi.client.v1.event.TradeEvent;
import com.huobi.client.v1.event.TradeStatisticsEvent;

public class WebSocketStreamClientImpl implements SubscriptionClient {

  private final SubscriptionOptions options;
  private WebSocketWatchDog watchDog;

  private final WebsocketRequestImpl requestImpl;

  private final List<WebSocketConnection> connections = new LinkedList<>();

  private final String apiKey;

  private final String secretKey;

  WebSocketStreamClientImpl(String apiKey, String secretKey, SubscriptionOptions options) {
    this.apiKey = apiKey;
    this.secretKey = secretKey;
    this.watchDog = null;
    this.options = Objects.requireNonNull(options);

    this.requestImpl = new WebsocketRequestImpl(apiKey);
  }

  private <T> void createConnection(WebsocketRequest<T> request) {
    if (watchDog == null) {
      watchDog = new WebSocketWatchDog(options);
    }
    WebSocketConnection connection = new WebSocketConnection(
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
      CandlestickInterval interval,
      SubscriptionListener<CandlestickEvent> callback) {
    subscribeCandlestickEvent(symbols, interval, callback, null);
  }


  @Override
  public void subscribeCandlestickEvent(
      String symbols,
      CandlestickInterval interval,
      SubscriptionListener<CandlestickEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeCandlestickEvent(
        parseSymbols(symbols), interval, subscriptionListener, errorHandler));
  }


  @Override
  public void subscribePriceDepthEvent(
      String symbols,
      SubscriptionListener<PriceDepthEvent> subscriptionListener) {
    subscribePriceDepthEvent(symbols, subscriptionListener, null);
  }

  @Override
  public void subscribePriceDepthEvent(
      String symbols,
      SubscriptionListener<PriceDepthEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribePriceDepthEvent(
        parseSymbols(symbols), subscriptionListener, errorHandler));
  }

  @Override
  public void subscribeTradeEvent(
      String symbols,
      SubscriptionListener<TradeEvent> subscriptionListener) {
    subscribeTradeEvent(symbols, subscriptionListener, null);
  }

  @Override
  public void subscribeTradeEvent(
      String symbols,
      SubscriptionListener<TradeEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeTradeEvent(
        parseSymbols(symbols), subscriptionListener, errorHandler));
  }

  @Override
  public void subscribeOrderUpdateEvent(
      String symbols,
      SubscriptionListener<OrderUpdateEvent> subscriptionListener) {
    subscribeOrderUpdateEvent(symbols, subscriptionListener, null);
  }

  @Override
  public void subscribeOrderUpdateEvent(
      String symbols,
      SubscriptionListener<OrderUpdateEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeOrderUpdateEvent(
        parseSymbols(symbols), subscriptionListener, errorHandler));
  }

  @Override
  public void unsubscribeAll() {
    for (WebSocketConnection connection : connections) {
      watchDog.onClosedNormally(connection);
      connection.close();
    }
    connections.clear();
  }

  @Override
  public void subscribeAccountEvent(
      BalanceMode mode,
      SubscriptionListener<AccountEvent> subscriptionListener) {
    subscribeAccountEvent(mode, subscriptionListener, null);
  }

  @Override
  public void subscribeAccountEvent(
      BalanceMode mode,
      SubscriptionListener<AccountEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeAccountEvent(mode, subscriptionListener, errorHandler));
  }

  @Override
  public void subscribe24HTradeStatisticsEvent(String symbols,
      SubscriptionListener<TradeStatisticsEvent> subscriptionListener) {
    subscribe24HTradeStatisticsEvent(symbols, subscriptionListener, null);
  }

  @Override
  public void subscribe24HTradeStatisticsEvent(String symbols,
      SubscriptionListener<TradeStatisticsEvent> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribe24HTradeStatisticsEvent(
        parseSymbols(symbols), subscriptionListener, errorHandler));
  }
}