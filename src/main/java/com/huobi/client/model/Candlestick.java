package com.huobi.client.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The candlestick/kline data.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Candlestick {

  /**
   * the UNIX formatted timestamp in UTC.
   */
  private long timestamp;

  /**
   * the aggregated trading volume in USDT.
   */
  private BigDecimal amount;

  /**
   * the number of completed trades.<br> it returns 0 when get ETF candlestick
   */
  private long count;

  /**
   * the opening price.
   */
  private BigDecimal open;

  /**
   * the closing price.
   */
  private BigDecimal close;

  /**
   * the low price.
   */
  private BigDecimal low;

  /**
   * the high price.
   */
  private BigDecimal high;

  /**
   * the trading volume in base currency.
   */
  private BigDecimal volume;
}
