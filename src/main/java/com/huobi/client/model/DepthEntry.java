package com.huobi.client.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * An depth entry consisting of price and amount.
 */
public class DepthEntry {

  /**
   * the price of the depth.
   */
  @Getter
  @Setter
  private BigDecimal price;

  /**
   * the amount of the depth.
   */
  @Getter
  @Setter
  private BigDecimal amount;
}
