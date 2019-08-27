package com.huobi.client.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.huobi.client.message.model.TradeOverviewEntry;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TradeOverviewMessage {

  /**
   * the UNIX formatted timestamp generated by server in UTC.
   */
  private long timestamp;


  /**
   * the TradeOverview.
   *
   * The TradeOverview data, see {@link TradeOverviewEntry}
   */
  private List<TradeOverviewEntry> overviewList;
}
