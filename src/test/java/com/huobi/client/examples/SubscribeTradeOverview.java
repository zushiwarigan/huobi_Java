package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.model.TradeOverview;

@Slf4j
public class SubscribeTradeOverview {

  public static void main(String[] args) {

    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeTradeOverviewEvent((tradeOverviewMessage) -> {

      log.info(tradeOverviewMessage.toString());
      for (TradeOverview overview : tradeOverviewMessage.getOverviewList()) {
        log.info("symbol:{} --> {}", overview.getSymbol(), overview.toString());
      }
    });


  }

}
