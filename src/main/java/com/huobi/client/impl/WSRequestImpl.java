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
import com.huobi.client.message.model.CandlestickEntry;
import com.huobi.client.message.model.TradeEntry;
import com.huobi.client.message.model.AggrTradeEntry;
import com.huobi.client.model.DepthEntry;
import com.huobi.client.model.PriceDepth;
import com.huobi.client.message.model.TradeOverviewEntry;
import com.huobi.client.message.model.TradeSummaryEntry;
import com.huobi.client.utils.ChannelUtil;
import com.huobi.client.utils.Handler;
import com.huobi.client.utils.TimeService;
import com.huobi.gateway.EventDecoder;
import com.huobi.gateway.EventDecoder.DepthTick;
import com.huobi.gateway.EventDecoder.OverviewTick;
import com.huobi.gateway.EventDecoder.ReqCandlestick.Tick;
import com.huobi.gateway.EventDecoder.ReqTrade;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthLevelEnum;
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
      EventDecoder.Candlestick candlestick = (EventDecoder.Candlestick) r.data;

      CandlestickMessage candlestickMessage = new CandlestickMessage();
      candlestickMessage.setInterval(interval);
      List<CandlestickEntry> dataList = new ArrayList<>();
      candlestickMessage.setSymbol(candlestick.symbol);
      candlestickMessage.setTimestamp(TimeService.convertCSTInMillisecondToUTC(candlestick.ts));

      CandlestickEntry data = CandlestickEntry.builder()
          .id(candlestick.id)
          .timestamp(TimeService.convertCSTInMillisecondToUTC(candlestick.ts))
          .open(new BigDecimal(candlestick.open))
          .close(new BigDecimal(candlestick.close))
          .high(new BigDecimal(candlestick.high))
          .low(new BigDecimal(candlestick.low))
          .volume(new BigDecimal(candlestick.volume))
          .turnover(new BigDecimal(candlestick.turnover))
          .numOfTrades(candlestick.numOfTrades)
          .build();
      dataList.add(data);

      candlestickMessage.setDataList(dataList);
      return candlestickMessage;
    };
    return request;
  }

  WSRequest<PriceDepthMessage> subscribePriceDepth(
      List<String> symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      SubscriptionListener<PriceDepthMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler, boolean actionReq) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<PriceDepthMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "PriceDepth for " + symbols;
    } else {
      request.name = "PriceDepth for " + symbols + " ...";
    }
    int depthLevelValue = depthLevel.level;
    request.connectionHandler = new Handler<WSConnection>() {
      @Override
      public void handle(WSConnection wsConnection) {
        for (String symbol : symbols) {
          String req = actionReq
              ? ChannelUtil.priceDepthReqChannel(symbol, depthLevelValue, depthStep)
              : ChannelUtil.priceDepthChannel(symbol, depthLevelValue, depthStep);

          wsConnection.send(req);
          await(1);
        }
      }
    };
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

      TradeEntry trade = new TradeEntry();
      trade.setTradeId(d.tradeId + "");
      trade.setTimestamp(d.ts);
      trade.setPrice(new BigDecimal(d.price));
      trade.setVolume(new BigDecimal(d.volume));
      trade.setDirection(TradeDirection.lookup(d.side.name().toLowerCase()));

      List<TradeEntry> list = new ArrayList<TradeEntry>();
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

      AggrTradeEntry trade = AggrTradeEntry.builder()
          .symbol(at.symbol)
          .timestamp(timestamp)
          .firstTradeId(at.firstTradeId)
          .lastTradeId(at.lastTradeId)
          .price(new BigDecimal(at.price))
          .volume(new BigDecimal(at.volume))
          .side(at.side)
          .build();
      List<AggrTradeEntry> list = new ArrayList<AggrTradeEntry>(1);
      list.add(trade);
      message.setAggrTradeList(list);
      return message;
    };
    return request;
  }


  WSRequest<TradeSummaryMessage> subscribeTradeSummary(
      List<String> symbols,
      SubscriptionListener<TradeSummaryMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler, boolean actionReq) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<TradeSummaryMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "TradeSummary for " + symbols;
    } else {
      request.name = "TradeSummary for " + symbols + " ...";
    }
    request.connectionHandler = new Handler<WSConnection>() {
      @Override
      public void handle(WSConnection wsConnection) {

        for (String symbol : symbols) {
          String req = actionReq
              ? ChannelUtil.tradeReqSummaryChannel(symbol)
              : ChannelUtil.tradeSummaryChannel(symbol);

          wsConnection.send(req);
          await(1);

        }
      }
    };
    request.parser = (r) -> {

      EventDecoder.Summary summary = (EventDecoder.Summary) r.data;
      long timestamp = TimeService.convertCSTInMillisecondToUTC(summary.ts);
      TradeSummaryMessage message = new TradeSummaryMessage();
      message.setTimestamp(timestamp);
      message.setSymbol(summary.symbol);

      TradeSummaryEntry tradeSummary = TradeSummaryEntry.builder()
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
      List<TradeOverviewEntry> overviewList = new ArrayList<>(tickList.size());
      for (OverviewTick tick : tickList) {
        overviewList.add(TradeOverviewEntry.builder()
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


  public WSRequest<CandlestickMessage> requestCandlestickEvent(
      List<String> symbols,
      Long from,
      Long to,
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
      request.name = "Req Candlestick for " + symbols;
    } else {
      request.name = "Req Candlestick for " + symbols + " ...";
    }

    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.candlestickReqChannel(symbol, from, to, interval))
            .forEach(req -> {
              System.out.println(" send message:[" + req + "]");
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {

      EventDecoder.ReqCandlestick reqCandlestick = (EventDecoder.ReqCandlestick) r.data;

      CandlestickMessage candlestickMessage = new CandlestickMessage();
      candlestickMessage.setInterval(interval);
      candlestickMessage.setSymbol(reqCandlestick.symbol);

      List<CandlestickEntry> dataList = new ArrayList<>();
      List<Tick> tickList = reqCandlestick.candlesticks;

      for (Tick tick : tickList) {
        CandlestickEntry data = CandlestickEntry.builder()
            .id(tick.id)
            .timestamp(TimeService.convertCSTInMillisecondToUTC(tick.ts))
            .open(new BigDecimal(tick.open))
            .close(new BigDecimal(tick.close))
            .high(new BigDecimal(tick.high))
            .low(new BigDecimal(tick.low))
            .volume(new BigDecimal(tick.volume))
            .turnover(new BigDecimal(tick.turnover))
            .numOfTrades(tick.numOfTrades)
            .build();
        dataList.add(data);
      }

      candlestickMessage.setDataList(dataList);
      return candlestickMessage;
    };
    return request;
  }


  WSRequest<TradeMessage> requestTrade(
      List<String> symbols, int limit,
      SubscriptionListener<TradeMessage> subscriptionListener,
      SubscriptionErrorHandler errorHandler) {
    InputChecker.checker().checkSymbolList(symbols).shouldNotNull(subscriptionListener, "listener");

    WSRequest<TradeMessage> request =
        new WSRequest<>(subscriptionListener, errorHandler);
    if (symbols.size() == 1) {
      request.name = "Req Trade for " + symbols;
    } else {
      request.name = "Req Trade for " + symbols + " ...";
    }
    request.connectionHandler = (connection) ->
        symbols.stream()
            .map((symbol) -> ChannelUtil.tradeReqChannel(symbol, limit))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.parser = (r) -> {
      EventDecoder.ReqTrade reqTrade = (EventDecoder.ReqTrade) r.data;
      TradeMessage tradeMessage = new TradeMessage();
      tradeMessage.setSymbol(reqTrade.symbol);

      List<ReqTrade.Tick> tickList = reqTrade.trades;
      List<TradeEntry> list = new ArrayList<>(tickList.size());
      for (ReqTrade.Tick tick : tickList) {
        TradeEntry trade = new TradeEntry();
        trade.setTradeId(tick.tradeId + "");
        trade.setTimestamp(tick.ts);
        trade.setPrice(new BigDecimal(tick.price));
        trade.setVolume(new BigDecimal(tick.volume));
        trade.setDirection(TradeDirection.lookup(tick.side.name().toLowerCase()));
        list.add(trade);
      }

      tradeMessage.setTradeList(list);
      return tradeMessage;
    };
    return request;
  }
}
