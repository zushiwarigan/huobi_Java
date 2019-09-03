package com.huobi.client.v2.message.entry;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeOverviewEntry {

  /**
   * The symbol like "btcusdt".
   */
  private String symbol;

  /**
   * The turnover of last 24 hours
   */
  private BigDecimal turnover;

  /**
   * The volume of last 24 hours
   */
  private BigDecimal volume;

  /**
   * The first price in current day
   */
  private BigDecimal open;

  /**
   * The last pricein current day
   */
  private BigDecimal close;

  /**
   * The highest price in current day
   */
  private BigDecimal high;

  /**
   * The lowest price in current day
   */
  private BigDecimal low;

  /**
   * The number of trades in last 24 hours
   */
  private Integer numOfTrades;
}