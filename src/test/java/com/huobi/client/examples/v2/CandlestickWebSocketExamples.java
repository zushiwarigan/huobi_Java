package com.huobi.client.examples.v2;

import com.alibaba.fastjson.JSON;

import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v2.HuobiClientFactory;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketClient;
import com.huobi.gateway.enums.CandlestickIntervalEnum;

public class CandlestickWebSocketExamples {


  public static void main(String[] args) {

    HuobiWebSocketClient client = HuobiClientFactory.newInstance().createWebSocketClient();

    HuobiWebSocketCallback<CandlestickMessage> callback = (candlestickMessage) -> {
      System.out.println("===========" + candlestickMessage.getSymbol() + "==========");
      System.out.println(JSON.toJSONString(candlestickMessage));

    };

    client.subscribeCandlestickEvent("btcusdt", CandlestickIntervalEnum.MIN_15, callback);
    client.requestCandlestickEvent("btcusdt", null, null, CandlestickIntervalEnum.MIN_15, callback);

  }

}
