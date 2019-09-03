package com.huobi.client.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HuobiWebSocketOptions {

  private String uri = "wss://api.huobi.pro/spot/v2/ws";

  private boolean isAutoReconnect = true;

  private int receiveLimitMs = 60_000;

  private int connectionDelayOnFailure = 15;

}
