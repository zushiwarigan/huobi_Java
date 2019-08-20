package com.huobi.client.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.huobi.client.SubscribeClient;
import com.huobi.client.SubscribeOption;
import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.model.enums.CandlestickInterval;
import com.huobi.client.model.message.CandlestickMessage;

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
  public void subscribeCandlestick(
      String symbols,
      CandlestickInterval interval,
      SubscriptionListener<CandlestickMessage> callback) {
    subscribeCandlestick(symbols, interval, callback, null);
  }

  @Override
  public void subscribeCandlestick(
      String symbols,
      CandlestickInterval interval,
      SubscriptionListener<CandlestickMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    createConnection(requestImpl.subscribeCandlestick(
        parseSymbols(symbols), interval, subscriptionListener, errorHandler));
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

