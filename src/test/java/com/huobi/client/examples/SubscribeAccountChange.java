package com.huobi.client.examples;

import lombok.extern.slf4j.Slf4j;

import com.huobi.client.v1.SubscriptionClient;
import com.huobi.client.v1.SubscriptionOptions;
import com.huobi.client.v1.model.AccountChange;
import com.huobi.client.v1.enums.BalanceMode;


@Slf4j
public class SubscribeAccountChange {

  public static void main(String[] args) {
    SubscriptionOptions options = new SubscriptionOptions();
    options.setUri("wss://api.huobi.pro");
    SubscriptionClient subscriptionClient = SubscriptionClient.create(
        "xxxxxx", "xxxxxx", options);
    subscriptionClient.subscribeAccountEvent(BalanceMode.AVAILABLE, (accountEvent) -> {
      log.info("---- Account Change: " + accountEvent.getChangeType() + " ----");
      for (AccountChange change : accountEvent.getAccountChangeList()) {
        log.info("Account: " + change.getAccountType());
        log.info("Currency: " + change.getCurrency());
        log.info("Balance: " + change.getBalance());
        log.info("Balance type: " + change.getBalanceType());
      }
    });
  }
}
