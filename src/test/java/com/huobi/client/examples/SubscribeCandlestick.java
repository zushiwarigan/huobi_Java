package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionClient;
import com.huobi.client.model.enums.CandlestickInterval;


@Slf4j
public class SubscribeCandlestick {

  public static void main(String[] args) {
    SubscriptionClient subscriptionClient = SubscriptionClient.create();

    //旧方式
    subscriptionClient.subscribeCandlestickEvent("btcusdt", CandlestickInterval.MIN15, (candlestickEvent) -> {
      log.info("Timestamp: " + candlestickEvent.getData().getTimestamp());
      log.info("High: " + candlestickEvent.getData().getHigh());
      log.info("Low: " + candlestickEvent.getData().getLow());
      log.info("Open: " + candlestickEvent.getData().getOpen());
      log.info("Close: " + candlestickEvent.getData().getClose());
      log.info("Volume: " + candlestickEvent.getData().getVolume());
    });

    //新方式
  }
}
