package com.huobi.gateway;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;


import com.huobi.gateway.protocol.MarketDownstreamProtocol;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Action;
import com.huobi.gateway.protocol.MarketDownstreamProtocol.Result;

public class EventDecoder {

  public static R decode(byte[] data) throws Exception {
    if (null == data) {
      return null;
    }

    R r = new R();
    Result result = Result.parseFrom(data);

    r.seq = result.getSequence() == 0 ? null : result.getSequence();
    r.code = result.getCode() == 0 ? null : result.getCode();
    r.message = "".equals(result.getMessage()) ? null : result.getMessage();
    r.action = result.getAction().name();
    r.ch = result.getCh();
    if (!result.getData().getValue().isEmpty()) {
      r.data = decodeData(result);
    }

    return r;
  }

  private static Object decodeData(Result result) throws Exception {
    if (result.getData() == null) {
      return null;
    }

    if (result.getAction() == Action.PING) {
      return decodePing(result);
    }

    String ch = result.getCh();
    if (ch.startsWith("candlestick")) {
      if (result.getAction() == Action.PUSH) {
        return decodeCandlestick(result);
      } else {
        return decodeReqCandlestick(result);
      }
    }

    if (ch.startsWith("mbp")) {
      return decodeMbp(result);
    }

    if (ch.startsWith("overview")) {
      return decodeOverview(result);
    }

    if (ch.startsWith("summary")) {
      return decodeSummary(result);
    }

    if (ch.startsWith("aggrTrade")) {
      return decodeAggrTrade(result);
    }

    if (ch.startsWith("trades")) {
      if (result.getAction() == Action.PUSH) {
        return decodeTrade(result);
      } else {
        return decodeReqTrade(result);
      }
    }
    return null;
  }

  private static Ping decodePing(Result result) throws Exception {
    MarketDownstreamProtocol.Ping p = result.getData().unpack(MarketDownstreamProtocol.Ping.class);

    Ping ping = new Ping();
    ping.ts = p.getTs();

    return ping;
  }

  private static Candlestick decodeCandlestick(Result result) throws Exception {
    MarketDownstreamProtocol.Candlestick d = result.getData().unpack(MarketDownstreamProtocol.Candlestick.class);

    Candlestick e = new Candlestick();
    e.close = d.getClose();
    e.high = d.getHigh();
    e.id = d.getId();
    e.symbol = d.getSymbol();
    e.open = d.getOpen();
    e.low = d.getLow();
    e.numOfTrades = d.getNumOfTrades();
    e.turnover = d.getTurnover();
    e.volume = d.getVolume();
    e.ts = d.getTs();
    return e;
  }

  public static ReqCandlestick decodeReqCandlestick(Result result) throws InvalidProtocolBufferException {
    MarketDownstreamProtocol.ReqCandlestick d = result.getData().unpack(MarketDownstreamProtocol.ReqCandlestick.class);

    ReqCandlestick e = new ReqCandlestick();
    e.symbol = d.getSymbol();
    List<ReqCandlestick.Tick> ticks = new ArrayList<>();
    for (MarketDownstreamProtocol.ReqCandlestick.Tick tick : d.getCandlesticksList()) {
      ReqCandlestick.Tick t = new ReqCandlestick.Tick();
      t.close = tick.getClose();
      t.high = tick.getHigh();
      t.id = tick.getId();
      t.open = tick.getOpen();
      t.low = tick.getLow();
      t.numOfTrades = tick.getNumOfTrades();
      t.turnover = tick.getTurnover();
      t.volume = tick.getVolume();
      t.ts = tick.getTs();

      ticks.add(t);
    }
    e.candlesticks = ticks;

    return e;
  }

  private static Depth decodeMbp(Result result) throws InvalidProtocolBufferException {
    if (result.getAction() == Action.PUSH || result.getAction() == Action.REQ) {
      MarketDownstreamProtocol.Depth d = result.getData().unpack(MarketDownstreamProtocol.Depth.class);
      Depth e = new Depth();
      e.symbol = d.getSymbol();
      e.delta = d.getDelta();

      for (MarketDownstreamProtocol.Depth.Tick tick : d.getBidsList()) {
        e.bids.add(new DepthTick(tick.getPrice(), tick.getSize()));
      }

      for (MarketDownstreamProtocol.Depth.Tick tick : d.getAsksList()) {
        e.asks.add(new DepthTick(tick.getPrice(), tick.getSize()));
      }

      return e;
    }
    return null;
  }

  private static AggrTrades decodeAggrTrade(Result result) throws InvalidProtocolBufferException {
    if (result.getAction() == Action.PUSH || result.getAction() == Action.REQ) {
      MarketDownstreamProtocol.AggrTrade d = result.getData().unpack(MarketDownstreamProtocol.AggrTrade.class);
      AggrTrades e = new AggrTrades();
      e.symbol = d.getSymbol();
      e.firstTradeId = d.getFirstTradeId();
      e.lastTradeId = d.getLastTradeId();
      e.ts = d.getTs();
      e.price = d.getPrice();
      e.volume = d.getVolume();
      e.side = SideEnum.valueOf(d.getSide().name());

      return e;
    }
    return null;
  }

  private static Trades decodeTrade(Result result) throws InvalidProtocolBufferException {
    MarketDownstreamProtocol.Trade d = result.getData().unpack(MarketDownstreamProtocol.Trade.class);
    Trades e = new Trades();
    e.symbol = d.getSymbol();
    e.tradeId = d.getTradeId();
    e.ts = d.getTs();
    e.price = d.getPrice();
    e.volume = d.getVolume();
    e.side = SideEnum.getByCode(d.getSideValue());

    return e;
  }

  private static ReqTrade decodeReqTrade(Result result) throws InvalidProtocolBufferException {
    MarketDownstreamProtocol.ReqTrade d = result.getData().unpack(MarketDownstreamProtocol.ReqTrade.class);
    ReqTrade e = new ReqTrade();
    e.symbol = d.getSymbol();
    List<ReqTrade.Tick> ticks = new ArrayList<>();
    for (MarketDownstreamProtocol.ReqTrade.Tick tick : d.getTradesList()) {
      ReqTrade.Tick t = new ReqTrade.Tick();
      t.price = tick.getPrice();
      t.volume = tick.getVolume();
      t.side = SideEnum.getByCode(tick.getSideValue());
      t.tradeId = tick.getTradeId();
      t.ts = tick.getTs();

      ticks.add(t);
    }
    e.trades = ticks;

    return e;
  }

  private static Overview decodeOverview(Result result) throws InvalidProtocolBufferException {
    if (result.getAction() == Action.PUSH) {
      MarketDownstreamProtocol.Overview d = result.getData().unpack(MarketDownstreamProtocol.Overview.class);

      Overview e = new Overview();
      e.ts = d.getTs();

      for (MarketDownstreamProtocol.Overview.Tick tick : d.getTickList()) {
        OverviewTick t = new OverviewTick();
        t.close = tick.getClose();
        t.high = tick.getHigh();
        t.symbol = tick.getSymbol();
        t.low = tick.getLow();
        t.numOfTrades = tick.getNumOfTrades();
        t.turnover = tick.getTurnover();
        t.volume = tick.getVolume();
        t.open = tick.getOpen();

        e.ticks.add(t);
      }

      return e;
    }
    return null;
  }

  private static Summary decodeSummary(Result result) throws InvalidProtocolBufferException {
    if (result.getAction() == Action.PUSH || result.getAction() == Action.REQ) {
      MarketDownstreamProtocol.MarketSummary d = result.getData().unpack(MarketDownstreamProtocol.MarketSummary.class);

      Summary e = new Summary();
      e.close = d.getClose();
      e.high = d.getHigh();
      e.id = d.getId();
      e.symbol = d.getSymbol();
      e.low = d.getLow();
      e.numOfTrades = d.getNumOfTrades();
      e.turnover = d.getTurnover();
      e.volume = d.getVolume();
      e.open = d.getOpen();
      e.ts = d.getTs();

      return e;
    }
    return null;
  }

  public static class R {

    public String action;
    public String ch;
    public Object data;
    public Long seq;
    public Integer code;

    public String message;
  }

  public static class AggrTrades {

    public String symbol;
    public long firstTradeId;
    public long lastTradeId;
    public long ts;
    public String price;
    public String volume;
    public SideEnum side;
  }

  public static class Trades {

    public String symbol;
    public long tradeId;
    public long ts;
    public String price;
    public String volume;
    public SideEnum side;
  }

  public static class ReqTrade {

    public String symbol;
    public List<Tick> trades;

    static class Tick {

      public long tradeId;
      public long ts;
      public String price;
      public String volume;
      public SideEnum side;
    }
  }

  public static class Depth {
    public long ts;
    public String symbol;
    public boolean delta;
    public List<DepthTick> bids = new ArrayList<>();
    public List<DepthTick> asks = new ArrayList<>();
  }

  public static class DepthTick {

    public String price;
    public String size;

    DepthTick(String price, String size) {
      this.price = price;
      this.size = size;
    }
  }

  public static class Overview {

    public long ts;
    public List<OverviewTick> ticks = new ArrayList<>();
  }

  public static class OverviewTick {

    public String symbol;
    public String open;
    public String close;
    public String low;
    public String high;
    public int numOfTrades;
    public String volume;
    public String turnover;
  }

  public static class Candlestick {

    public long id;
    public String symbol;
    public long ts;
    public String open;
    public String close;
    public String low;
    public String high;
    public int numOfTrades;
    public String volume;
    public String turnover;
  }

  public static class ReqCandlestick {

    public String symbol;
    public List<Tick> candlesticks;

    static class Tick {

      public long id;
      public long ts;
      public String open;
      public String close;
      public String low;
      public String high;
      public int numOfTrades;
      public String volume;
      public String turnover;
    }
  }

  public static class Summary {

    public long id;
    public String symbol;
    public long ts;
    public String open;
    public String close;
    public String low;
    public String high;
    public int numOfTrades;
    public String volume;
    public String turnover;
  }

  public static class Ping {

    public long ts;
  }
}
