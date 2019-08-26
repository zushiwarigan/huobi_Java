package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.model.Trade;

@Slf4j
public class SubscribeTrades {

  public static void main(String[] args) {

    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeTradeEvent("btcusdt", (tradeMessage) -> {

      for (Trade trade : tradeMessage.getTradeList()) {
        log.info(" tradeId:{}", trade.getTradeId());
        log.info(" direction:{}", trade.getDirection().toString());
        log.info(" price:{}", trade.getPrice().toPlainString());
        log.info(" amount:{}", trade.getAmount().toPlainString());
        log.info(" timestamp:{}", trade.getTimestamp());
      }


    });


  }

}
