package com.huobi.client.v1;

import com.huobi.client.v1.impl.HuobiApiInternalFactory;
import com.huobi.client.v1.message.AggrTradesMessage;
import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.client.v1.message.TradeMessage;
import com.huobi.client.v1.message.TradeOverviewMessage;
import com.huobi.client.v1.message.TradeSummaryMessage;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthLevelEnum;
import com.huobi.gateway.enums.DepthStepEnum;

/***
 * The subscription client interface, it is used for subscribing any market data update and
 * account change, it is asynchronous, so you must implement the SubscriptionListener interface.
 * The server will push any update to the client. if client get the update, the onReceive method
 * will be called.
 */
public interface SubscribeClient {

  /**
   * Subscribe candlestick/kline event. If the candlestick/kline is updated, server will send the data to client and onReceive in callback will be
   * called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param interval The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback);

  /**
   * Subscribe candlestick/kline event. If the candlestick/kline is updated, server will send the data to client and onReceive in callback will be
   * called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param interval The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Subscribe price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   * @apiNote this interface will be use default depth level(DepthLevelEnum.LEVEL_5) and default depth step（DepthStepEnum.STEP0）
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribePriceDepthEvent(String symbols,SubscriptionListener<PriceDepthMessage> callback);


  /**
   * Subscribe price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param depthLevel The levels of price depth
   * @param depthStep The aggregate price level by same precision or in a same range precision
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,SubscriptionListener<PriceDepthMessage> callback);

  /**
   * Subscribe price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param depthLevel The levels of price depth
   * @param depthStep The aggregate price level by same precision or in a same range precision
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Subscribe new trades event. If the new trade happened, server will send the trade message to client, and client will onReceive in callback..
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeTradeEvent(String symbols, SubscriptionListener<TradeMessage> callback);

  /**
   * Subscribe new trades event. If the new trade happened, server will send the trade message to client, and client will onReceive in callback..
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribeTradeEvent(String symbols,
      SubscriptionListener<TradeMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Subscribe AggrTrades event . If the new trade happened ,server will send the aggregate of same taker price, and client will onReceive in callback..
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeAggrTradesEvent(String symbols, SubscriptionListener<AggrTradesMessage> callback);

  /**
   * Subscribe AggrTrades event . If the new trade happened ,server will send the aggregate of same taker price, and client will onReceive in callback..
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribeAggrTradesEvent(String symbols, SubscriptionListener<AggrTradesMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Subscribe 24 hours trade summary event . If summary is generated, server will send the data to client and onReceive in callback will be called.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback);

  /**
   * Subscribe 24 hours trade summary event . If summary is generated, server will send the data to client and onReceive in callback will be called.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribeTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Subscribe all the symbol overview . If symbol's overview change, server will send data to client and onReceive in callback.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void subscribeTradeOverviewEvent(SubscriptionListener<TradeOverviewMessage> callback);

  /**
   * Subscribe all the symbol overview . If symbol's overview change, server will send data to client and onReceive in callback.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void subscribeTradeOverviewEvent(SubscriptionListener<TradeOverviewMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Request the KLine data from server.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param from Get data from this UNIX timestamp
   * @param to Get data end of this UNIX timestamp
   * @param interval The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void requestCandlestickEvent(String symbols, Long from, Long to,
      CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback);

  /**
   * Request the KLine data from server.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param from Get data from this UNIX timestamp
   * @param to Get data end of this UNIX timestamp
   * @param interval The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void requestCandlestickEvent(String symbols, Long from, Long to,
      CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Request price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void requestPriceDepth(String symbols,SubscriptionListener<PriceDepthMessage> callback);

  /**
   * Request price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param depthLevel The levels of price depth
   * @param depthStep The aggregate price level by same precision or in a same range precision
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void requestPriceDepth(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> callback);

  /**
   * Request price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param depthLevel The levels of price depth
   * @param depthStep The aggregate price level by same precision or in a same range precision
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void requestPriceDepth(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Request new trades event. If the new trade happened, server will send the trade message to client, and client will onReceive in callback..
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param limit The number of one request
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void requestTradeEvent(String symbols,int limit,
      SubscriptionListener<TradeMessage> callback);

  /**
   * Request new trades event. If the new trade happened, server will send the trade message to client, and client will onReceive in callback..
   *
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param limit The number of one request
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void requestTradeEvent(String symbols,int limit,
      SubscriptionListener<TradeMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Request 24 hours trade summary event . If summary is generated, server will send the data to client and onReceive in callback will be called.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   */
  void requestTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback);

  /**
   * Request 24 hours trade summary event . If summary is generated, server will send the data to client and onReceive in callback will be called.
   * @param symbols The symbols, like "btcusdt". Use comma to separate multi symbols, like "btcusdt,ethusdt".
   * @param callback The implementation is required. onReceive will be called if receive server's update.
   * @param errorHandler The error handler will be called if subscription failed or error happen between client and Huobi server.
   */
  void requestTradeSummaryEvent(String symbols, SubscriptionListener<TradeSummaryMessage> callback,
      SubscriptionErrorHandler errorHandler);

  /**
   * Unsubscribe all subscription.
   */
  void unsubscribeAll();

  /**
   * Create the subscription client to subscribe the update from server.
   *
   * @return The instance of synchronous client.
   */
  static SubscribeClient create() {
    return create("", "", new SubscribeOption());
  }

  /**
   * Create the subscription client to subscribe the update from server.
   *
   * @param apiKey The public key applied from Huobi.
   * @param secretKey The private key applied from Huobi.
   * @return The instance of synchronous client.
   */
  static SubscribeClient create(
      String apiKey, String secretKey) {
    return HuobiApiInternalFactory.getInstance().createSubscribeClient(
        apiKey, secretKey, new SubscribeOption());
  }

  /**
   * Create the subscription client to subscribe the update from server.
   *
   * @param apiKey The public key applied from Huobi.
   * @param secretKey The private key applied from Huobi.
   * @param subscribeOption The option of subscription connection, see {@link SubscriptionOptions}
   * @return The instance of synchronous client.
   */
  static SubscribeClient create(
      String apiKey, String secretKey, SubscribeOption subscribeOption) {
    return HuobiApiInternalFactory.getInstance().createSubscribeClient(
        apiKey, secretKey, subscribeOption);
  }

}
