package com.huobi.client.v1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
 */
@AllArgsConstructor
@ToString
public enum CandlestickInterval {
  MIN1("1min"),
  MIN5("5min"),
  MIN15("15min"),
  MIN30("30min"),
  MIN60("60min"),
  DAY1("1day"),
  MON1("1mon"),
  WEEK1("1week"),
  YEAR1("1year");

  @Getter
  private final String code;

  @Override
  public String toString() {
    return code;
  }
}
