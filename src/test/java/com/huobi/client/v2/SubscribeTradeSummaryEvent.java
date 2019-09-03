package com.huobi.client.v2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscribeTradeSummaryEvent {

  public static void main(String[] args) {
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();

    client.subscribeTradeSummaryEvent("btcusdt",(tradeSummaryMessage -> {
      log.info("====[Sub]==========={}==============",tradeSummaryMessage.getSymbol());
      log.info(tradeSummaryMessage.toString());
    }));

    client.subscribeTradeSummaryEvent("ethusdt",(tradeSummaryMessage -> {
      log.info("====[Sub]==========={}==============",tradeSummaryMessage.getSymbol());
      log.info(tradeSummaryMessage.toString());
    }));

    client.requestTradeSummaryEvent("btcusdt",(tradeSummaryMessage -> {
      log.info("====[Req]==========={}==============",tradeSummaryMessage.getSymbol());
      log.info(tradeSummaryMessage.toString());
    }));
  }

}
