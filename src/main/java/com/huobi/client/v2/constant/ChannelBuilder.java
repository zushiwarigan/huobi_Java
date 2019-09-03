package com.huobi.client.v2.constant;

import java.util.HashMap;
import java.util.Map;

import com.huobi.client.v2.dto.ReqChannel;
import com.huobi.client.v2.dto.SubChannel;
import com.huobi.client.v2.enums.CandlestickIntervalEnum;
import com.huobi.client.v2.enums.DepthLevelEnum;
import com.huobi.client.v2.enums.DepthStepEnum;

public class ChannelBuilder {

  private static final String ACTION_SUB = "sub";
  private static final String ACTION_REQ = "req";

  /**
   * Candlestick channel
   */

  public static String buildCandlestickChannel(String symbol, CandlestickIntervalEnum interval) {
    String ch = getCandlestickChannel(symbol, interval);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String buildCandlestickReqChannel(String symbol, Long from, Long to, CandlestickIntervalEnum interval) {
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

  /**
   * PriceDepth channel
   */

  public static String buildPriceDepthChannel(String symbol, DepthLevelEnum level, DepthStepEnum step) {
    String ch = getPriceDepthChannel(symbol, level, step);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String buildPriceDepthReqChannel(String symbol, DepthLevelEnum level, DepthStepEnum step) {
    String ch = getPriceDepthChannel(symbol, level, step);
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).ch(ch).build();
    return channel.toJSONString();
  }

  public static String getPriceDepthChannel(String symbol, DepthLevelEnum level, DepthStepEnum step) {
    String ch = "mbp#" + symbol + '@' + level.getLevel();
    if (step != null) {
      ch += '.' + step.value;
    }
    return ch;
  }

  /**
   * Trades channel
   */

  public static String buildTradeChannel(String symbol) {
    String ch = getTradeChannel(symbol);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String buildTradeReqChannel(String symbol, int limit) {
    String ch = getTradeChannel(symbol);
    Map<String, Object> params = new HashMap<>();
    params.put("limit", limit);
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).ch(ch).params(params).build();
    return channel.toJSONString();
  }

  public static String getTradeChannel(String symbol) {
    return "trades#" + symbol;
  }

  /**
   * AggrTrades channel
   */
  public static String buildAggrTradesChannel(String symbol) {
    String ch = "aggrTrades#" + symbol;
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  /**
   * TradeOverview channel
   */
  public static String buildTradeOverviewChannel() {
    String ch = "overview";
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  /**
   * TradeSummary channel
   */

  public static String buildTradeSummaryChannel(String symbol) {
    String ch = getTradeSummaryChannel(symbol);
    SubChannel channel = SubChannel.builder().action(ACTION_SUB).ch(ch).build();
    return channel.toJSONString();
  }

  public static String buildTradeReqSummaryChannel(String symbol) {
    String ch = getTradeSummaryChannel(symbol);
    ReqChannel channel = ReqChannel.builder().seq(System.currentTimeMillis()).action(ACTION_REQ).ch(ch).build();
    return channel.toJSONString();
  }

  public static String getTradeSummaryChannel(String symbol) {
    return "summary#" + symbol;
  }
}
