package com.huobi.client.v2;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;

@Slf4j
public class SubscribePriceDepthEvent {

  public static void main(String[] args) {
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();

    client.subscribePriceDepthEvent("btcusdt", (priceDepthMessage -> {
      log.info("===[Sub]======{}==========", priceDepthMessage.getSymbol());
      priceDepthMessage.getAsks().forEach(priceLevelEntry -> {
        log.info("== asks == price:{} size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize().toPlainString());
      });

      log.info("****************************************");

      priceDepthMessage.getBids().forEach(priceLevelEntry -> {
        log.info("== bids == price:{} size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize().toPlainString());
      });
    }));

    client.requestPriceDepthEvent("btcusdt", DepthLevelEnum.LEVEL_5, DepthStepEnum.STEP0, (priceDepthMessage -> {
      log.info("===[Req]======{}==========", priceDepthMessage.getSymbol());
      priceDepthMessage.getAsks().forEach(priceLevelEntry -> {
        log.info("== asks == price:{} size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize().toPlainString());
      });

      log.info("****************************************");

      priceDepthMessage.getBids().forEach(priceLevelEntry -> {
        log.info("== bids == price:{} size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize().toPlainString());
      });
    }));


  }

}
