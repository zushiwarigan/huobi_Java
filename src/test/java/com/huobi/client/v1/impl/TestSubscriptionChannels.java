package com.huobi.client.v1.impl;

import static org.junit.Assert.assertEquals;

import com.huobi.client.v1.utils.Channels;
import com.huobi.client.v1.utils.TimeService;
import com.huobi.client.v1.enums.CandlestickInterval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TimeService.class})
public class TestSubscriptionChannels {

  static {
    PowerMockito.mockStatic(TimeService.class);
    PowerMockito.when(TimeService.getCurrentTimeStamp()).thenReturn(new Long(123));
  }

  @Test
  public void testklineChannel() {
    assertEquals("{\"sub\":\"market.btcusdt.kline.1min\",\"id\":\"123\"}",
        Channels.klineChannel("btcusdt", CandlestickInterval.MIN1));
  }
}
