package com.huobi.client.v2;

import com.huobi.client.v2.enums.CandlestickIntervalEnum;
import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;
import com.huobi.client.v2.impl.HuobiMarketWebSocketClientImpl;
import com.huobi.client.v2.message.AggrTradesMessage;
import com.huobi.client.v2.message.CandlestickMessage;
import com.huobi.client.v2.message.PriceDepthMessage;
import com.huobi.client.v2.message.TradeMessage;
import com.huobi.client.v2.message.TradeOverviewMessage;
import com.huobi.client.v2.message.TradeSummaryMessage;

public interface HuobiMarketWebSocketClient {

  /**
   * Subscribe candlestick/kline event. If the candlestick/kline is updated, server will send the data to client and onReceive in callback will be
   * called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param interval The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback<CandlestickMessage> callback);

  void requestCandlestickEvent(String symbols, CandlestickIntervalEnum interval, Long from, Long to,
      HuobiWebSocketCallback<CandlestickMessage> callback);


  void subscribePriceDepthEvent(String symbols, HuobiWebSocketCallback<PriceDepthMessage> callback);

  void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback);

  void requestPriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback);


  void subscribeTradeEvent(String symbols, HuobiWebSocketCallback<TradeMessage> callback);

  void requestTradeEvent(String symbols,int limit, HuobiWebSocketCallback<TradeMessage> callback);

  void subscribeAggrTradesEvent(String symbols, HuobiWebSocketCallback<AggrTradesMessage> callback);

  void subscribeTradeOverviewEvent(HuobiWebSocketCallback<TradeOverviewMessage> callback);

  void subscribeTradeSummaryEvent(String symbols, HuobiWebSocketCallback<TradeSummaryMessage> callback);

  void requestTradeSummaryEvent(String symbols, HuobiWebSocketCallback<TradeSummaryMessage> callback);

  static HuobiMarketWebSocketClient createMarketWebSocket() {
    return new HuobiMarketWebSocketClientImpl(new HuobiWebSocketOptions());
  }

}
