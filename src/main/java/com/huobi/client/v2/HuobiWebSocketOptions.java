package com.huobi.client.v2;

import lombok.Getter;
import lombok.Setter;

public class HuobiWebSocketOptions {

  @Getter
  @Setter
  private String uri = "wss://api.huobi.pro/spot/v2/ws";

  @Getter
  @Setter
  private boolean autoReconnect = true;
}
