package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionClient;


@Slf4j
public class SubscribeMarketDepth {

  public static void main(String[] args) {
    SubscriptionClient subscriptionClient = SubscriptionClient.create();
    subscriptionClient.subscribePriceDepthEvent("btcusdt", (priceDepthEvent) -> {
      log.info("bids 0 price: " + priceDepthEvent.getData().getBids().get(0).getPrice());
      log.info("bids 0 volume: " + priceDepthEvent.getData().getBids().get(0).getAmount());

    });
  }
}
