package com.huobi.client.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.channel.ReqChannel;
import com.huobi.client.channel.SubChannel;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthStepEnum;


@Slf4j
public abstract class ChannelUtil {

  public static final String ACTION_SUB = "sub";
  public static final String ACTION_REQ = "req";

  public static String candlestickChannel(String symbol, CandlestickIntervalEnum interval) {
    String ch = getCandlestickChannel(symbol, interval);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String candlestickReqChannel(String symbol, Long from, Long to, CandlestickIntervalEnum interval) {
    String ch = getCandlestickChannel(symbol, interval);
    Map<String, Object> params = new HashMap<>();
    if (from != null) {
      params.put("from", from);
    }
    if (to != null) {
      params.put("to", to);
    }
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).params(params).ch(ch).build();
    return channel.toJSONString();
  }

  public static String getCandlestickChannel(String symbol, CandlestickIntervalEnum interval) {
    return "candlestick#" + symbol + '@' + interval.value;
  }

  public static String priceDepthChannel(String symbol, int levels, DepthStepEnum step) {
    String ch = getPriceDepthChannel(symbol, levels, step);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String priceDepthReqChannel(String symbol, int levels, DepthStepEnum step) {
    String ch = getPriceDepthChannel(symbol, levels, step);
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).ch(ch).build();
    return channel.toJSONString();
  }

  public static String getPriceDepthChannel(String symbol, int levels, DepthStepEnum step) {
    String ch = "mbp#" + symbol + '@' + levels;
    if (step != null) {
      ch += '.' + step.value;
    }
    return ch;
  }

  public static String tradeChannel(String symbol) {
    String ch = getTradeChannel(symbol);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String tradeReqChannel(String symbol, int limit) {
    String ch = getTradeChannel(symbol);
    Map<String,Object> params = new HashMap<>();
    params.put("limit",limit);
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).ch(ch).params(params).build();
    return channel.toJSONString();
  }

  public static String getTradeChannel(String symbol) {
    return "trades#" + symbol;
  }

  public static String aggrTradesChannel(String symbol) {
    String ch = "aggrTrades#" + symbol;
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String tradeSummaryChannel(String symbol) {
    String ch = "summary#" + symbol;
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String tradeSummaryChannel() {
    String ch = "overview";
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

}
