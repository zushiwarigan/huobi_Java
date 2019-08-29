package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v1.SubscribeClient;
import com.huobi.client.v1.message.model.TradeEntry;

@Slf4j
public class SubscribeTrades {

  public static void main(String[] args) {

    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeTradeEvent("btcusdt", (tradeMessage) -> {
      log.info("=====[sub]===={}====={}===",tradeMessage.getSymbol(),tradeMessage.getTradeList().size());
      for (TradeEntry trade : tradeMessage.getTradeList()) {
        log.info(" tradeId:{}", trade.getTradeId());
        log.info(" direction:{}", trade.getDirection().toString());
        log.info(" price:{}", trade.getPrice().toPlainString());
        log.info(" volume:{}", trade.getVolume().toPlainString());
        log.info(" timestamp:{}", trade.getTimestamp());
      }
    });


    subscribeClient.requestTradeEvent("btcusdt",20, (tradeMessage) -> {
      log.info("=====[req]===={}====={}===",tradeMessage.getSymbol(),tradeMessage.getTradeList().size());
      for (TradeEntry trade : tradeMessage.getTradeList()) {
        log.info(" tradeId:{}", trade.getTradeId());
        log.info(" direction:{}", trade.getDirection().toString());
        log.info(" price:{}", trade.getPrice().toPlainString());
        log.info(" volume:{}", trade.getVolume().toPlainString());
        log.info(" timestamp:{}", trade.getTimestamp());
      }
    });


  }

}
