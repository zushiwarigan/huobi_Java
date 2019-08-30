package com.huobi.client.v1.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.huobi.client.v1.message.model.AggrTradeEntry;

/**
 * The aggregate trades received by subscription of aggrTrades
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AggrTradesMessage {

  /**
   * Get the symbol you subscribed.
   *
   * like "btcusdt".
   */
  private String symbol;

  /**
   * the UNIX formatted timestamp generated by server in UTC.
   */
  private Long timestamp;

  /**
   * the AggrTrade.
   *
   * The AggrTrade data, see {@link AggrTradeEntry}
   */
  private List<AggrTradeEntry> aggrTradeList;

}