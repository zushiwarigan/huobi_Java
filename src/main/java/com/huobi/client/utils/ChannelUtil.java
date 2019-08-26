package com.huobi.client.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.channel.SubChannel;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthStepEnum;


@Slf4j
public abstract class ChannelUtil {

  public static String candlestickChannel(String symbol, CandlestickIntervalEnum interval) {
    String ch = getCandlestickChannel(symbol,interval);
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

  public static String getCandlestickChannel(String symbol, CandlestickIntervalEnum interval){
    return "candlestick#" + symbol + '@' + interval.value;
  }

  public static String priceDepthChannel(String symbol, int levels, DepthStepEnum step) {

    String ch = "mbp#" + symbol + '@' + levels;
    if (step != null) {
      ch += '.' + step.value;
    }
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

  public static String tradeChannel(String symbol) {
    String ch = "trades#" + symbol;
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

  public static String aggrTradesChannel(String symbol) {
    String ch = "aggrTrades#" + symbol;
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

  public static String tradeSummaryChannel(String symbol) {
    String ch = "summary#" + symbol;
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

  public static String tradeSummaryChannel() {
    String ch = "overview";
    SubChannel channel = SubChannel.builder().action("sub").ch(ch).build();
    return JSON.toJSONString(channel);
  }

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
