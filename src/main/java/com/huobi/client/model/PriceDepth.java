package com.huobi.client.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The price depth information.
 */
public class PriceDepth {

  /**
   * the UNIX formatted timestamp in UTC.
   *
   * The timestamp.
   */
  @Getter
  @Setter
  private long timestamp;

  /**
   * the list of the bid depth.
   *
   * The price depth list, see {@link DepthEntry}
   */
  @Getter
  @Setter
  private List<DepthEntry> bids;

  /**
   * the list of the ask depth.
   *
   * The price depth list, see {@link DepthEntry}
   */
  @Getter
  @Setter
  private List<DepthEntry> asks;
}
