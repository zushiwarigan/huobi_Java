package com.huobi.client.v2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscribeAggrTradesEvent {

  public static void main(String[] args) {
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();

    client.subscribeAggrTradesEvent("btcusdt",(aggrTradesMessage -> {
      log.info(aggrTradesMessage.toString());
    }));
  }

}
