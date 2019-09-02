package com.huobi.client.examples.v2;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.client.v1.message.model.PriceLevelEntry;
import com.huobi.client.v2.HuobiClientFactory;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketClient;
import com.huobi.gateway.enums.DepthLevelEnum;
import com.huobi.gateway.enums.DepthStepEnum;

@Slf4j
public class PriceDepthWebSocketExamples {


  public static void main(String[] args) {

    HuobiWebSocketClient client = HuobiClientFactory.newInstance().createWebSocketClient();

    HuobiWebSocketCallback<PriceDepthMessage> callback = (priceDepthMessage) -> {
      log.info("=====[Price Depth Sub]========{}====={}===", priceDepthMessage.getSymbol(), priceDepthMessage.getAsks().size());
      PriceLevelEntry bid0 = priceDepthMessage.getBids().get(0);
      PriceLevelEntry ask0 = priceDepthMessage.getAsks().get(0);
      log.info("bids 0 price:{} size:{}", bid0.getPrice().toPlainString(), bid0.getSize().toPlainString());
      log.info("asks 0 price:{} size:{}", ask0.getPrice().toPlainString(), ask0.getSize().toPlainString());
    };

    HuobiWebSocketCallback<PriceDepthMessage> callback1 = (priceDepthMessage) -> {
      log.info("=====[Price Depth Req]========{}====={}===", priceDepthMessage.getSymbol(), priceDepthMessage.getAsks().size());

      List<PriceLevelEntry> asks = priceDepthMessage.getAsks();
      asks.forEach(priceLevelEntry -> {
        log.info(" ask price:{}  size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize());
      });

      log.info("=========================");

      List<PriceLevelEntry> bids = priceDepthMessage.getBids();
      bids.forEach(priceLevelEntry -> {
        log.info(" bis price:{}  size:{}", priceLevelEntry.getPrice().toPlainString(), priceLevelEntry.getSize());
      });

    };

    client.subscribePriceDepthEvent("btcusdt", callback);

    client.subscribePriceDepthEvent("btcusdt", DepthLevelEnum.LEVEL_10, DepthStepEnum.STEP1, callback);

    client.requestPriceDepth("btcusdt", callback1);

  }

}
