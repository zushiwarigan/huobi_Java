package com.huobi.client.examples.v2;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.client.v1.message.model.PriceLevelEntry;
import com.huobi.client.v2.HuobiClientFactory;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketClient;

@Slf4j
public class PriceDepthWebSocketExamples {


  public static void main(String[] args) {

    HuobiWebSocketClient client = HuobiClientFactory.newInstance().createWebSocketClient();

    HuobiWebSocketCallback<PriceDepthMessage> callback = (priceDepthMessage) -> {
      log.info("=====[Price Depth]========{}========", priceDepthMessage.getSymbol());
      PriceLevelEntry bid0 = priceDepthMessage.getBids().get(0);
      PriceLevelEntry ask0 = priceDepthMessage.getAsks().get(0);
      log.info("bids 0 price:{} size:{}", bid0.getPrice().toPlainString(), bid0.getSize().toPlainString());
      log.info("asks 0 price:{} size:{}", ask0.getPrice().toPlainString(), ask0.getSize().toPlainString());

    };

    client.subscribePriceDepthEvent("btcusdt", callback);
    client.subscribePriceDepthEvent("ethusdt", callback);
    client.subscribePriceDepthEvent("eosusdt", callback);
    client.subscribePriceDepthEvent("ltcusdt", callback);
    client.subscribePriceDepthEvent("etcusdt", callback);
  }

}
