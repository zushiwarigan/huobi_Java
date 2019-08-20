package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionClient;
import com.huobi.client.SubscriptionOptions;


@Slf4j
public class Subscribe24HTradeStatistics {

  public static void main(String[] args) {
    // Subscribe 24HTradeStatistics with custom server
    SubscriptionOptions options = new SubscriptionOptions();
    options.setUri("wss://api.huobi.pro");
    SubscriptionClient subscriptionClient = SubscriptionClient.create("", "", options);
    subscriptionClient.subscribe24HTradeStatisticsEvent("btcusdt", (statisticsEvent) -> {
      log.info("Timestamp: " + statisticsEvent.getTradeStatistics().getTimestamp());
      log.info("High: " + statisticsEvent.getTradeStatistics().getHigh());
      log.info("Low: " + statisticsEvent.getTradeStatistics().getLow());
      log.info("Open: " + statisticsEvent.getTradeStatistics().getOpen());
      log.info("Close: " + statisticsEvent.getTradeStatistics().getClose());
      log.info("Volume: " + statisticsEvent.getTradeStatistics().getVolume());
    });
  }
}
