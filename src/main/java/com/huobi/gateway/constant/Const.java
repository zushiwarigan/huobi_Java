package com.huobi.gateway.constant;

import java.time.ZoneId;

import com.huobi.gateway.enums.DepthLevelEnum;


public interface Const {

  // 交易所编码参数名称
  String EXCHANGE_CODE = "exchangeCode";

  // 数据编码名称
  String CODEC = "X-HB-Codec";

  // 交易所pro的id
  long EXCHANGE_PRO_ID = 1;

  // 交易所pro的code
  String EXCHANGE_PRO_CODE = "pro";

  // 交易所hadax的id
  long EXCHANGE_HADAX_ID = 2;

  // 交易所hadax的code
  String EXCHANGE_HADAX_CODE = "hadax";

  /**
   * huobi10 指数
   */
  String SYMBOLS_TYPE_INDEX_HUOBI10 = "huobi10";

  /**
   * hb10 基金净值
   */
  String SYMBOLS_TYPE_INDEX_HB10 = "hb10";

  ZoneId DEFAULT_ZONE = ZoneId.of("GMT+08:00");

  // 无效exId
  long INVALID_EXCHANGE_ID = 0;

  // 无效instId
  long INVALID_INST_ID = 0;

  // Datadog mentric
  // session 存活数量
  String STAT_WS_LIVE = "client.ws.live";
  // req请求数量
  String STAT_WS_REQ = "client.ws.req";
  // req请求时间
  String STAT_REQ_EB_TS = "client.req.eb.ts";
  // req请求时间
  String STAT_REQ_HANDLE_TS = "client.req.handle.ts";

  String STAT_WS_CLOSE = "client.ws.close";
  // gateway向client推送消息量
  String STAT_WS_SUB_PUSH = "client.sub.send";

  // rest kline
  String STAT_REST_KLINE = "rest.kline";

  // req请求阈值
  int STAT_REQ_THRESHOLD = 20;

  // rest 请求阈值
  int STAT_REST_THRESHOLD = 50;

  //系统最小时间
  long SYSTEM_TIME = 1325347200L;

  /**
   * 单次查询K线的最大数量
   */
  int CANDLESTICK_LIMIT = 1500;

  /**
   * 深度p10类型限制大小
   */
  int DEPTH_P10_LIMIT = 200;

  // 数据库查询最小间隔
  long DB_QUERY_MIN_INTERVAL = 120000L;

  // 交易对分片数量
  int SYMBOL_SHARDING = 16;

  // 客户端连接分片数量
  int CLIENT_SHARDING = 8;

  // 盘口深度的档位种类
  int DEPTH_LEVELS = DepthLevelEnum.values().length;

  // 深度数据增量推送频率
  int DEPTH_DELTA_NUM = 5;

}


