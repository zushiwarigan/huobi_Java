package com.huobi.client.v2;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v2.message.entry.TradeOverviewEntry;

@Slf4j
public class SubscribeTradeOverviewEvent {

  public static void main(String[] args) {
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();
    client.subscribeTradeOverviewEvent((tradeOverviewMessage -> {
      List<TradeOverviewEntry> entryList = tradeOverviewMessage.getOverviewList();
      log.info("====[Sub]========={}==========",entryList.size());
      entryList.forEach(tradeOverviewEntry -> {
        log.info(tradeOverviewEntry.toString());
      });

    }));
  }

}
