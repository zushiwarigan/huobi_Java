package com.huobi.client.utils;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.message.CandlestickChannel;
import com.huobi.gateway.enums.CandlestickIntervalEnum;


@Slf4j
public abstract class ChannelUtil {
  public static String candlestickChannel(String symbol, CandlestickIntervalEnum interval) {
    Gson gson = new Gson();
    String ch = "candlestick#" + symbol + '@' + interval.value;
    CandlestickChannel channel = CandlestickChannel.builder().action("sub").ch(ch).build();
    String jsonChannel = gson.toJson(channel, CandlestickChannel.class);
    return jsonChannel;
  }

//  public static String priceDepthChannel(String symbol) {
//    JSONObject json = new JSONObject();
//    json.put("sub", "market." + symbol + ".depth.step0");
//    json.put("id", TimeService.getCurrentTimeStamp() + "");
//    return json.toJSONString();
//  }
//
//  public static String tradeChannel(String symbol) {
//    JSONObject json = new JSONObject();
//    json.put("sub", "market." + symbol + ".trade.detail");
//    json.put("id", TimeService.getCurrentTimeStamp() + "");
//    return json.toJSONString();
//  }
//
//  public static String accountChannel(BalanceMode mode) {
//    JSONObject json = new JSONObject();
//    json.put("op", "sub");
//    json.put("cid", TimeService.getCurrentTimeStamp() + "");
//    json.put("topic", "accounts");
//    if (mode != null) {
//      json.put("model", mode.getCode());
//    }
//    return json.toJSONString();
//  }
//
//  public static String ordersChannel(String symbol) {
//    JSONObject json = new JSONObject();
//    json.put("op", "sub");
//    json.put("cid", TimeService.getCurrentTimeStamp() + "");
//    json.put("topic", "orders." + symbol);
//    return json.toJSONString();
//  }
//
//  public static String tradeStatisticsChannel(String symbol) {
//    JSONObject json = new JSONObject();
//    json.put("sub", "market." + symbol + ".detail");
//    json.put("id", TimeService.getCurrentTimeStamp() + "");
//    return json.toJSONString();
//  }
}
