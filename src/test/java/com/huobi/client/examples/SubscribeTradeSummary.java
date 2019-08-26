package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;

@Slf4j
public class SubscribeTradeSummary {

  public static void main(String[] args) {

    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeTradeSummaryEvent("btcusdt", (tradeSummaryMessage) -> {

      log.info(tradeSummaryMessage.getTradeSummary().toString());
    });


  }

}
