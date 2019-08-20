package com.huobi.client.enums;

import com.huobi.client.utils.EnumLookup;

/**
 * working, lock.
 */
public enum AccountState {
  WORKING("working"),
  LOCK("lock");

  private final String code;

  AccountState(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }

  private static final EnumLookup<AccountState> lookup = new EnumLookup<>(AccountState.class);

  public static AccountState lookup(String name) {
    return lookup.lookup(name);
  }
}
