package com.huobi.client.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.huobi.client.SubscriptionListener;
import com.huobi.client.enums.AccountType;
import com.huobi.client.enums.BalanceMode;
import com.huobi.client.enums.BalanceType;

/**
 * The account change information received by subscription of account.
 */
public class AccountChange {

  /**
   * the currency of the change.
   *
   * The currency type, like "btc".
   */
  @Getter
  @Setter
  private String currency = "";

  /**
   * the account of the change.
   *
   * The account type like "SPOT", "OTC".
   */
  @Getter
  @Setter
  private AccountType accountType;

  /**
   * the balance after the change. If the {@link BalanceMode} in {@link com.huobi.client.SubscribeClient#subscribeAccount(BalanceMode,
   * SubscriptionListener)} is AVAILABLE, the balance refers to available balance. If the {@link BalanceMode} is TOTAL, the balance refers to total
   * balance for trade sub account (available+frozen)
   *
   * The balance value.
   */
  @Getter
  @Setter
  private BigDecimal balance = new BigDecimal(0.0);

  /**
   * the balance type.
   *
   * The balance type, like trade, loan, interest, see {@link BalanceType}
   */
  @Getter
  @Setter
  private BalanceType balanceType;
}
