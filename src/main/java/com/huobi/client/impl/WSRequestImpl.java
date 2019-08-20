package com.huobi.client.impl;

import java.util.List;

import com.huobi.client.SubscriptionErrorHandler;
import com.huobi.client.SubscriptionListener;
import com.huobi.client.utils.Channels;
import com.huobi.client.utils.JsonWrapper;
import com.huobi.client.utils.TimeService;
import com.huobi.client.model.Candlestick;
import com.huobi.client.enums.CandlestickInterval;
import com.huobi.client.message.CandlestickMessage;

import static com.huobi.client.utils.InternalUtils.await;

public class WSRequestImpl {

  private final String apiKey;

  public WSRequestImpl(String apiKey) {
    this.apiKey = apiKey;
  }


  public WSRequest<CandlestickMessage> subscribeCandlestick(
      List<String> symbols,
      CandlestickInterval interval,
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
            .map((symbol) -> Channels.klineChannel(symbol, interval))
            .forEach(req -> {
              connection.send(req);
              await(1);
            });
    request.jsonParser = (jsonWrapper) -> {
      String ch = jsonWrapper.getString("ch");
      ChannelParser parser = new ChannelParser(ch);
      CandlestickMessage candlestickMessage = new CandlestickMessage();
      candlestickMessage.setSymbol(parser.getSymbol());
      candlestickMessage.setInterval(interval);
      candlestickMessage.setTimestamp(
          TimeService.convertCSTInMillisecondToUTC(jsonWrapper.getLong("ts")));
      JsonWrapper tick = jsonWrapper.getJsonObject("tick");
      Candlestick data = new Candlestick();
      data.setTimestamp(TimeService.convertCSTInSecondToUTC(tick.getLong("id")));
      data.setOpen(tick.getBigDecimal("open"));
      data.setClose(tick.getBigDecimal("close"));
      data.setLow(tick.getBigDecimal("low"));
      data.setHigh(tick.getBigDecimal("high"));
      data.setAmount(tick.getBigDecimal("amount"));
      data.setCount(tick.getLong("count"));
      data.setVolume(tick.getBigDecimal("vol"));
      candlestickMessage.setData(data);
      return candlestickMessage;
    };
    return request;
  }

}
