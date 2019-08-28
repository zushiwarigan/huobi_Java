package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.SubscriptionClient;


@Slf4j
public class SubscribeMarketDepth {

  public static void main(String[] args) {
    //旧方式
//    SubscriptionClient subscriptionClient = SubscriptionClient.create();
//    subscriptionClient.subscribePriceDepthEvent("btcusdt", (priceDepthEvent) -> {
//      log.info("bids 0 price: " + priceDepthEvent.getData().getBids().get(0).getPrice());
//      log.info("bids 0 volume: " + priceDepthEvent.getData().getBids().get(0).getAmount());
//    });

    //新方式 订阅price depth
    SubscribeClient subscribeClient = SubscribeClient.create();
//    subscribeClient.subscribePriceDepthEvent("btcusdt", (priceDepthMessage) -> {
//    log.info("====[sub]====={}=========",priceDepthMessage.getSymbol());
//      log.info("bids 0 price: " + priceDepthMessage.getBids().get(0).getPrice());
//      log.info("bids 0 size: " + priceDepthMessage.getBids().get(0).getSize());
//      log.info("asks 0 price: " + priceDepthMessage.getAsks().get(0).getPrice());
//      log.info("asks 0 size: " + priceDepthMessage.getAsks().get(0).getSize());
//    });

    //新方式 请求price depth
    subscribeClient.requestPriceDepth("btcusdt", (priceDepthMessage) -> {
      log.info("====[req]====={}=========",priceDepthMessage.getSymbol());
      log.info("bids 0 price: " + priceDepthMessage.getBids().get(0).getPrice());
      log.info("bids 0 size: " + priceDepthMessage.getBids().get(0).getSize());
      log.info("asks 0 price: " + priceDepthMessage.getAsks().get(0).getPrice());
      log.info("asks 0 size: " + priceDepthMessage.getAsks().get(0).getSize());
    });
  }
}
