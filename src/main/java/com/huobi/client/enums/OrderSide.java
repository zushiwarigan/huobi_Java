package com.huobi.client.enums;

/**
 * buy, sell, both.
 */

public enum OrderSide {
  BUY("buy"),
  SELL("sell");

  private final String code;

  OrderSide(String side) {
    this.code = side;
  }

  @Override
  public String toString() {
    return code;
  }


}