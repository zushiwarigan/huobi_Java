package com.huobi.client.v2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.huobi.client.v2.impl.HuobiWebSocketConnection;
import com.huobi.client.v2.impl.HuobiWebSocketListener.ConnectState;

public class WebSocketWatchDog {

  private static long FIXED_DAILY_TIME = 1_000;

  private static long EXECUTE_AWAIT_TIME = 5_000;


  private ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

  public WebSocketWatchDog(HuobiWebSocketConnection connection) {

    if (!connection.getOptions().isAutoReconnect()) {
      return;
    }

    // 定时任务watch dog
    exec.scheduleAtFixedRate(() -> {

      if (connection.getListener().getState() == ConnectState.FAILURE) {
        connection.reConnect();
        await(EXECUTE_AWAIT_TIME);
      }

    }, FIXED_DAILY_TIME, FIXED_DAILY_TIME, TimeUnit.MILLISECONDS);

  }


  public void await(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
    }
  }

}
