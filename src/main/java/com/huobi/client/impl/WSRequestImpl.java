package com.huobi.client.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.enums.TradeDirection;
import com.huobi.client.message.AggrTradesMessage;
import com.huobi.client.message.CandlestickMessage;
import com.huobi.client.message.PriceDepthMessage;
import com.huobi.client.message.TradeMessage;
import com.huobi.client.message.TradeOverviewMessage;
import com.huobi.client.message.TradeSummaryMessage;
import com.huobi.client.model.AggrTrade;
import com.huobi.client.model.Candlestick;
import com.huobi.client.model.DepthEntry;
import com.huobi.client.model.PriceDepth;
import com.huobi.client.model.Trade;
import com.huobi.client.model.TradeOverview;
import com.huobi.client.model.TradeSummary;
import com.huobi.client.utils.ChannelUtil;
import com.huobi.client.utils.Handler;
import com.huobi.client.utils.TimeService;
import com.huobi.gateway.EventDecoder;
import com.huobi.gateway.EventDecoder.DepthTick;
import com.huobi.gateway.EventDecoder.OverviewTick;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthStepEnum;

import static com.huobi.client.utils.InternalUtils.await;


@Slf4j
@AllArgsConstructor
public class WSRequestImpl {

  private final String apiKey;

  public WSRequest<CandlestickMessage> subscribeCandlestick(
      List<String> symbols,
      CandlestickIntervalEnum interval,
      SubscriptionListener<CandlestickMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker()
        .checkSymbolList(symbols)
        .shouldNotNull(subscriptionListener, "listener")
        .shouldNotNull(interval, "CandlestickInterval");
    WSRequest<CandlestickMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "Candlestick for " + symbols;
    } else {
      request.name = "Candlestick for " + symbols + " ...";
    }

    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.candlestickChannel(symbol, interval))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {
      EventDecoder.Candlestick d = (EventDecoder.Candlestick) r.data;
      CandlestickMessage candlestickMessage = new CandlestickMessage();
      candlestickMessage.setSymbol(d.symbol);
      candlestickMessage.setInterval(interval);
      candlestickMessage.setTimestamp(
          TimeService.convertCSTInMillisecondToUTC(d.ts));
      Candlestick data = new Candlestick();
      data.setTimestamp(TimeService.convertCSTInSecondToUTC(d.ts));
      data.setOpen(new BigDecimal(d.open));
      data.setClose(new BigDecimal(d.close));
      data.setLow(new BigDecimal(d.low));
      data.setHigh(new BigDecimal(d.high));
      data.setAmount(new BigDecimal(d.turnover));
      data.setCount(d.numOfTrades);
      data.setVolume(new BigDecimal(d.volume));
      candlestickMessage.setData(data);
      return candlestickMessage;
    };
    return request;
  }

  WSRequest<PriceDepthMessage> subscribePriceDepth(
      List<String> symbols,
      SubscriptionListener<PriceDepthMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<PriceDepthMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "PriceDepth for " + symbols;
    } else {
      request.name = "PriceDepth for " + symbols + " ...";
    }
    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.priceDepthChannel(symbol, 5, DepthStepEnum.STEP0))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {
      EventDecoder.Depth d = (EventDecoder.Depth) r.data;
      PriceDepthMessage priceDepthMessage = new PriceDepthMessage();
      priceDepthMessage.setTimestamp(
          TimeService.convertCSTInMillisecondToUTC(d.ts));
      priceDepthMessage.setSymbol(d.symbol);
      PriceDepth priceDepth = new PriceDepth();
      priceDepth.setTimestamp(TimeService.convertCSTInMillisecondToUTC(d.ts));
      List<DepthEntry> bidList = new LinkedList<>();
      List<DepthTick> bids = d.bids;
      bids.forEach((item) -> {
        DepthEntry depthEntry = new DepthEntry();
        depthEntry.setPrice(new BigDecimal(item.price));
        depthEntry.setAmount(new BigDecimal(item.size));
        bidList.add(depthEntry);
      });
      List<DepthEntry> askList = new LinkedList<>();
      List<DepthTick> asks = d.asks;
      asks.forEach((item) -> {
        DepthEntry depthEntry = new DepthEntry();
        depthEntry.setPrice(new BigDecimal(item.price));
        depthEntry.setAmount(new BigDecimal(item.size));
        askList.add(depthEntry);
      });
      priceDepth.setAsks(askList);
      priceDepth.setBids(bidList);
      priceDepthMessage.setData(priceDepth);
      return priceDepthMessage;
    };
    return request;
  }


  WSRequest<TradeMessage> subscribeTrade(
      List<String> symbols,
      SubscriptionListener<TradeMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<TradeMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "Trade for " + symbols;
    } else {
      request.name = "Trade for " + symbols + " ...";
    }
    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.tradeChannel(symbol))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {
      EventDecoder.Trades d = (EventDecoder.Trades) r.data;
      TradeMessage tradeMessage = new TradeMessage();
      tradeMessage.setTimestamp(TimeService.convertCSTInMillisecondToUTC(d.ts));
      tradeMessage.setSymbol(d.symbol);

      Trade trade = new Trade();
      trade.setTradeId(d.tradeId + "");
      trade.setTimestamp(d.ts);
      trade.setPrice(new BigDecimal(d.price));
      trade.setAmount(new BigDecimal(d.volume));
      trade.setDirection(TradeDirection.lookup(d.side.name().toLowerCase()));

      List<Trade> list = new ArrayList<Trade>();
      list.add(trade);
      tradeMessage.setTradeList(list);
      return tradeMessage;
    };
    return request;
  }


  WSRequest<AggrTradesMessage> subscribeAggrTrades(
      List<String> symbols,
      SubscriptionListener<AggrTradesMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<AggrTradesMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "AggrTrades for " + symbols;
    } else {
      request.name = "AggrTrades for " + symbols + " ...";
    }
    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.aggrTradesChannel(symbol))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {

      EventDecoder.AggrTrades at = (EventDecoder.AggrTrades) r.data;
      long timestamp = TimeService.convertCSTInMillisecondToUTC(at.ts);
      AggrTradesMessage message = new AggrTradesMessage();
      message.setTimestamp(timestamp);
      message.setSymbol(at.symbol);

      AggrTrade trade = AggrTrade.builder()
          .symbol(at.symbol)
          .timestamp(timestamp)
          .firstTradeId(at.firstTradeId)
          .lastTradeId(at.lastTradeId)
          .price(new BigDecimal(at.price))
          .volume(new BigDecimal(at.volume))
          .side(at.side)
          .build();
      List<AggrTrade> list = new ArrayList<AggrTrade>(1);
      list.add(trade);
      message.setAggrTradeList(list);
      return message;
    };
    return request;
  }


  WSRequest<TradeSummaryMessage> subscribeTradeSummary(
      List<String> symbols,
      SubscriptionListener<TradeSummaryMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<TradeSummaryMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "TradeSummary for " + symbols;
    } else {
      request.name = "TradeSummary for " + symbols + " ...";
    }
    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.tradeSummaryChannel(symbol))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {

      EventDecoder.Summary summary = (EventDecoder.Summary) r.data;
      long timestamp = TimeService.convertCSTInMillisecondToUTC(summary.ts);
      TradeSummaryMessage message = new TradeSummaryMessage();
      message.setTimestamp(timestamp);
      message.setSymbol(summary.symbol);

      TradeSummary tradeSummary = TradeSummary.builder()
          .symbol(summary.symbol)
          .timestamp(timestamp)
          .turnover(new BigDecimal(summary.turnover))
          .volume(new BigDecimal(summary.volume))
          .open(new BigDecimal(summary.open))
          .close(new BigDecimal(summary.close))
          .high(new BigDecimal(summary.high))
          .low(new BigDecimal(summary.low))
          .numOfTrades(summary.numOfTrades)
          .build();

      message.setTradeSummary(tradeSummary);
      return message;
    };
    return request;
  }


  WSRequest<TradeOverviewMessage> subscribeTradeOverview(
      SubscriptionListener<TradeOverviewMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().shouldNotNull(subscriptionListener, "listener");

    WSRequest<TradeOverviewMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    request.name = "TradeOverview";

    request.connectionHandler = new Handler<WSConnection>() {
      @Override
      public void handle(WSConnection connection) {
        String req = ChannelUtil.tradeSummaryChannel();
        connection.send(req);
      }
    };

    request.parser = (r) -> {

      EventDecoder.Overview overview = (EventDecoder.Overview) r.data;
      long timestamp = TimeService.convertCSTInMillisecondToUTC(overview.ts);
      TradeOverviewMessage message = new TradeOverviewMessage();
      message.setTimestamp(timestamp);

      List<OverviewTick> tickList = overview.ticks;
      List<TradeOverview> overviewList = new ArrayList<>(tickList.size());
      for (OverviewTick tick : tickList) {
        overviewList.add(TradeOverview.builder()
            .symbol(tick.symbol)
            .turnover(new BigDecimal(tick.turnover))
            .volume(new BigDecimal(tick.volume))
            .open(new BigDecimal(tick.open))
            .close(new BigDecimal(tick.close))
            .high(new BigDecimal(tick.high))
            .low(new BigDecimal(tick.low))
            .numOfTrades(tick.numOfTrades)
            .build());
      }

      message.setOverviewList(overviewList);
      return message;
    };
    return request;
  }
}
