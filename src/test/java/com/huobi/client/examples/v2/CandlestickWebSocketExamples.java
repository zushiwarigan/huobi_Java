package com.huobi.client.examples.v2;

import com.alibaba.fastjson.JSON;
import okhttp3.WebSocket;

import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v2.HuobiClientFactory;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketClient;
import com.huobi.client.v2.impl.HuobiWebSocketListener.ConnectState;
import com.huobi.gateway.enums.CandlestickIntervalEnum;

public class CandlestickWebSocketExamples {


  public static void main(String[] args) {

    HuobiWebSocketClient client = HuobiClientFactory.newInstance().createWebSocketClient();

    HuobiWebSocketCallback<CandlestickMessage> callback = (candlestickMessage) -> {
      System.out.println("===========" + candlestickMessage.getSymbol() + "==========");
      System.out.println(JSON.toJSONString(candlestickMessage));

    };

    client.subscribeCandlestickEvent("btcusdt", CandlestickIntervalEnum.MIN_15, callback);
//    client.requestCandlestickEvent("btcusdt", null, null, CandlestickIntervalEnum.MIN_15, callback);

//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        try {
//          while (true) {
//            long waitTime = 1000;
//            if (client.getConnectState() == ConnectState.FAILURE) {
//              client.reConnect();
//              waitTime = 5000;
//            }
//
//            Thread.sleep(waitTime);
//
//          }
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//    }).start();
  }

}
