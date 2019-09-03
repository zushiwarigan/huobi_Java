package com.huobi.client.v2;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v2.enums.CandlestickIntervalEnum;
import com.huobi.client.v2.message.entry.CandlestickEntry;

@Slf4j
public class SubscribeCandlestickEvent {

  public static void main(String[] args) {
    // Create WebSocket Client
    HuobiMarketWebSocketClient client = HuobiMarketWebSocketClient.createMarketWebSocket();

    // WebSocket Subscribe Candlestick
    client.subscribeCandlestickEvent("btcusdt" ,CandlestickIntervalEnum.MIN_15,  (candlestickMessage -> {
      List<CandlestickEntry> entryList = candlestickMessage.getDataList();
      log.info("===[Sub]========{}=========={}=====",candlestickMessage.getSymbol(),entryList.size());
      entryList.forEach(candlestickEntry -> {
        log.info(candlestickEntry.toString());
      });
    }));

    // WebSocket Request Candlestick
    client.requestCandlestickEvent("btcusdt", CandlestickIntervalEnum.MIN_15, null, null, (candlestickMessage -> {
      List<CandlestickEntry> entryList = candlestickMessage.getDataList();
      log.info("===[Req]========{}=========={}=====",candlestickMessage.getSymbol(),entryList.size());
      entryList.forEach(candlestickEntry -> {
        log.info(candlestickEntry.toString());
      });
    }));


  }
}
