package com.huobi.client.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.message.CandlestickMessage;
import com.huobi.client.message.PriceDepthMessage;
import com.huobi.client.model.Candlestick;
import com.huobi.client.model.DepthEntry;
import com.huobi.client.model.PriceDepth;
import com.huobi.client.utils.ChannelUtil;
import com.huobi.client.utils.TimeService;
import com.huobi.gateway.EventDecoder;
import com.huobi.gateway.EventDecoder.DepthTick;
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
    request.rParser = (r) -> {
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
    request.rParser = (r) -> {
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
}
