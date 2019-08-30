package com.huobi.client.v2;

import com.huobi.client.v1.message.CandlestickMessage;
import com.huobi.client.v1.message.PriceDepthMessage;
import com.huobi.gateway.enums.CandlestickIntervalEnum;
import com.huobi.gateway.enums.DepthLevelEnum;
import com.huobi.gateway.enums.DepthStepEnum;

public interface HuobiWebSocketClient {

  void subscribeCandlestickEvent(String symbols, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback<CandlestickMessage> callback);

  void requestCandlestickEvent(String symbols, Long from, Long to, CandlestickIntervalEnum interval,
      HuobiWebSocketCallback<CandlestickMessage> callback);

  void subscribePriceDepthEvent(String symbols, HuobiWebSocketCallback<PriceDepthMessage> callback);


  void subscribePriceDepthEvent(String symbols, DepthLevelEnum depthLevel, DepthStepEnum depthStep,
      HuobiWebSocketCallback<PriceDepthMessage> callback);

}
