package com.huobi.client.v1.enums;

import com.huobi.client.v1.utils.EnumLookup;

/**
 * SUBMITTED, PARTIALFILLED, CANCELLING. PARTIALCANCELED FILLED CANCELED
 */
public enum OrderState {
  SUBMITTED("submitted"),
  PARTIALFILLED("partial-filled"),
  CANCELLING("cancelling"),
  PARTIALCANCELED("partial-canceled"),
  FILLED("filled"),
  CANCELED("canceled");


  private final String code;

  OrderState(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }

  private static final EnumLookup<OrderState> lookup = new EnumLookup<>(OrderState.class);

  public static OrderState lookup(String name) {
    return lookup.lookup(name);
  }
}
