package com.huobi.client.v1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.huobi.client.v1.utils.EnumLookup;

/**
 * The event that Asset Change Notification Related ,for example : create order (order.place) ,
 * commit order (order.match),order refunds（order.refund),order canceled (order.cancel) ,card
 * deducts transaction fee （order.fee-refund),lever account transfer（margin.transfer),loan
 * principal（margin.loan),loan interest （margin.interest),return loan interest(margin.repay),other
 * asset change(other)
 */
@AllArgsConstructor
public enum AccountChangeType {

  NEWORDER("order.place"),

  TRADE("order.match"),

  REFUND("order.refund"),

  CANCELORDER("order.cancel"),

  FEE("order.fee-refund"),

  TRANSFER("margin.transfer"),

  LOAN("margin.loan"),

  INTEREST("margin.interest"),

  REPAY("margin.repay"),

  OTHER("other"),

  INVALID("INVALID");

  @Getter
  private final String code;

  private static final EnumLookup<AccountChangeType> lookup = new EnumLookup<>(
      AccountChangeType.class);

  public static AccountChangeType lookup(String name) {
    return lookup.lookup(name);
  }

  @Override
  public String toString() {
    return code;
  }
}
