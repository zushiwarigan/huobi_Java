package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.SubscriptionClient;
import com.huobi.client.enums.CandlestickInterval;
import com.huobi.gateway.enums.CandlestickIntervalEnum;


@Slf4j
public class SubscribeCandlestick {

  public static void main(String[] args) {
    //旧方式
    SubscriptionClient subscriptionClient = SubscriptionClient.create();
    subscriptionClient.subscribeCandlestickEvent("btcusdt", CandlestickInterval.MIN15, (candlestickEvent) -> {
      log.info("Timestamp: " + candlestickEvent.getData().getTimestamp());
      log.info("High: " + candlestickEvent.getData().getHigh());
      log.info("Low: " + candlestickEvent.getData().getLow());
      log.info("Open: " + candlestickEvent.getData().getOpen());
      log.info("Close: " + candlestickEvent.getData().getClose());
      log.info("Volume: " + candlestickEvent.getData().getVolume());
    });

    //新方式
    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeCandlestick("btcusdt", CandlestickIntervalEnum.MIN_1, (candlestickMessage) -> {
      log.info("Timestamp: " + candlestickMessage.getData().getTimestamp());
      log.info("High: " + candlestickMessage.getData().getHigh());
      log.info("Low: " + candlestickMessage.getData().getLow());
      log.info("Open: " + candlestickMessage.getData().getOpen());
      log.info("Close: " + candlestickMessage.getData().getClose());
      log.info("Volume: " + candlestickMessage.getData().getVolume());
    });
  }
}
