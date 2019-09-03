package com.huobi.client.v2.message.entry;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceLevelEntry {

  /**
   * the price of the level.
   */
  private BigDecimal price;

  /**
   * the size of the level.
   */
  private BigDecimal size;
}
