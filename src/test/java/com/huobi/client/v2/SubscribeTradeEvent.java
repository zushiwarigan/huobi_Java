package com.huobi.client.v2;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;
import com.huobi.client.v2.message.entry.TradeEntry;

@Slf4j
public class SubscribeTradeEvent {

  public static void main(String[] args) {
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();

    client.subscribeTradeEvent("btcusdt", (tradeMessage -> {
      List<TradeEntry> list = tradeMessage.getTradeList();
      log.info("===[Sub]======{}======={}===", tradeMessage.getSymbol(),list.size());
      list.forEach(tradeEntry -> {
        log.info(tradeEntry.toString());
      });

    }));

    client.requestTradeEvent("btcusdt", 10, (tradeMessage -> {
      List<TradeEntry> list = tradeMessage.getTradeList();
      log.info("===[Req]======{}======={}===", tradeMessage.getSymbol(),list.size());
      list.forEach(tradeEntry -> {
        log.info(tradeEntry.toString());
      });
    }));
  }

}
