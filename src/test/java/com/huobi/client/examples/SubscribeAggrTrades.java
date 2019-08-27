package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.message.model.AggrTradeEntry;

@Slf4j
public class SubscribeAggrTrades {

  public static void main(String[] args) {

    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeAggrTradesEvent("btcusdt", (aggrTradesMessage) -> {

      for (AggrTradeEntry trade : aggrTradesMessage.getAggrTradeList()) {
        log.info(" trade:{}", trade.toString());
      }


    });


  }

}
