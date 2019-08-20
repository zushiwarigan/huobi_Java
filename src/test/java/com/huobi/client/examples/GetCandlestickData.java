package com.huobi.client.examples;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.AsyncRequestClient;
import com.huobi.client.SyncRequestClient;
import com.huobi.client.model.Candlestick;
import com.huobi.client.enums.CandlestickInterval;

@Slf4j
public class GetCandlestickData {
  public static void main(String[] args) {
    // Synchronization mode
    SyncRequestClient syncRequestClient = SyncRequestClient.create();
    List<Candlestick> candlestickList = syncRequestClient.getLatestCandlestick(
        "btcusdt", CandlestickInterval.MIN1, 1);
    log.info("---- 1 min candlestick for btcusdt ----");
    for (Candlestick candlestick : candlestickList) {
      log.info("Timestamp: " + candlestick.getTimestamp());
      log.info("High: " + candlestick.getHigh());
      log.info("Low: " + candlestick.getLow());
      log.info("Open: " + candlestick.getOpen());
      log.info("Close: " + candlestick.getClose());
      log.info("Volume: " + candlestick.getVolume());
    }
    // Asynchronization mode
    AsyncRequestClient asyncRequestClient = AsyncRequestClient.create();
    asyncRequestClient.getLatestCandlestick("btcusdt", CandlestickInterval.MIN1, 1, (candlestickResult) -> {
      if (candlestickResult.succeeded()) {
        log.info("---- 1 min candlestick for btcusdt ----");
        for (Candlestick candlestick : candlestickResult.getData()) {
          log.info("Timestamp: " + candlestick.getTimestamp());
          log.info("High: " + candlestick.getHigh());
          log.info("Low: " + candlestick.getLow());
          log.info("Open: " + candlestick.getOpen());
          log.info("Close: " + candlestick.getClose());
          log.info("Volume: " + candlestick.getVolume());
        }
      }
    });
  }
}
