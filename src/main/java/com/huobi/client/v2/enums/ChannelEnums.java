package com.huobi.client.v2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ChannelEnums {

  CANDLESTICK("candlestick"),
  PRICE_DEPTH("mbp"),
  ;
  private String channelName;
}
