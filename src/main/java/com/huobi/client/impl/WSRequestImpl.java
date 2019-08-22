package com.huobi.client.impl;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.message.CandlestickMessage;
import com.huobi.client.model.Candlestick;
import com.huobi.client.utils.ChannelUtil;
import com.huobi.client.utils.TimeService;
import com.huobi.gateway.EventDecoder;
import com.huobi.gateway.enums.CandlestickIntervalEnum;

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

}
