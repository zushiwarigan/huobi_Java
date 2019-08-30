package com.huobi.client.v2;


import java.util.concurrent.atomic.AtomicInteger;

import com.huobi.client.v2.impl.HuobiWebSocketClientImpl;

public class HuobiClientFactory {

  private String apiKey;

  private String secretKey;

  private HuobiClientFactory() {}

  private HuobiClientFactory(String apiKey, String secretKey) {
    this.apiKey = apiKey;
    this.secretKey = secretKey;
  }

  public static HuobiClientFactory newInstance() {
    return new HuobiClientFactory();
  }

  public static HuobiClientFactory newInstance(String apiKey, String secretKey) {
    return new HuobiClientFactory(apiKey, secretKey);
  }

  public static HuobiWebSocketClient createWebSocketClient(HuobiWebSocketOptions options) {
    return new HuobiWebSocketClientImpl(options);
  }

  public HuobiWebSocketClient createWebSocketClient() {
    return new HuobiWebSocketClientImpl(new HuobiWebSocketOptions());
  }


}
