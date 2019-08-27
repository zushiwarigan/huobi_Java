package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscribeClient;
import com.huobi.client.SubscriptionClient;
import com.huobi.client.enums.CandlestickInterval;
import com.huobi.client.message.model.CandlestickEntry;
import com.huobi.gateway.enums.CandlestickIntervalEnum;


@Slf4j
public class SubscribeCandlestick {

  public static void main(String[] args) {
    //旧方式
    SubscriptionClient subscriptionClient = SubscriptionClient.create();
    subscriptionClient.subscribeCandlestickEvent("btcusdt", CandlestickInterval.MIN15, (candlestickEvent) -> {
      log.info("Timestamp: " + candlestickEvent.getData().getTimestamp());
      log.info("High: " + candlestickEvent.getData().getHigh());
      log.info("Low: " + candlestickEvent.getData().getLow());
      log.info("Open: " + candlestickEvent.getData().getOpen());
      log.info("Close: " + candlestickEvent.getData().getClose());
      log.info("Volume: " + candlestickEvent.getData().getVolume());
    });

    //新方式 订阅K线数据
    SubscribeClient subscribeClient = SubscribeClient.create();
    subscribeClient.subscribeCandlestickEvent("btcusdt", CandlestickIntervalEnum.MIN_15, (candlestickMessage) -> {
      log.info("=====[sub]====={}==========", candlestickMessage.getSymbol());
      for (CandlestickEntry data : candlestickMessage.getDataList()) {
        log.info("Timestamp: " + data.getTimestamp());
        log.info("High: " + data.getHigh());
        log.info("Low: " + data.getLow());
        log.info("Open: " + data.getOpen());
        log.info("Close: " + data.getClose());
        log.info("Volume: " + data.getVolume());
      }
    });

    //新方式 请求K线数据
    subscribeClient.requestCandlestickEvent("btcusdt", 1566841500L,1566843300L , CandlestickIntervalEnum.MIN_15, (candlestickMessage) -> {
      log.info("=====[req]====={}======{}====", candlestickMessage.getSymbol(), candlestickMessage.getDataList().size());
      for (CandlestickEntry data : candlestickMessage.getDataList()) {
        log.info("Timestamp: " + data.getTimestamp());
        log.info("High: " + data.getHigh());
        log.info("Low: " + data.getLow());
        log.info("Open: " + data.getOpen());
        log.info("Close: " + data.getClose());
        log.info("Volume: " + data.getVolume());
      }
    });
  }
}
