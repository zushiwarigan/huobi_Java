package com.huobi.client.message.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The candlestick/kline data.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class CandlestickEntry {

  /**
   * The timestamp of open time
   */
  private Long id;
  /**
   * the UNIX formatted timestamp in UTC.
   */
  private long timestamp;

  /**
   * the aggregated trading volume in USDT.
   */
  private BigDecimal turnover;

  /**
   * the number of completed trades.<br> it returns 0 when get ETF candlestick
   */
  private long numOfTrades;

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
