package com.huobi.client.v2.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.huobi.client.impl.utils.Handler;
import com.huobi.client.v2.HuobiWebSocketCallback;
import com.huobi.client.v2.HuobiWebSocketParser;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HuobiWebSocketRequest<T> {

  private Handler<HuobiWebSocketConnection> connectionHandler;
  private Handler<HuobiWebSocketConnection> authHandler = null;
  private HuobiWebSocketCallback<T> callback;
  private HuobiWebSocketParser parser;

}
