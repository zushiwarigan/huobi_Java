package com.huobi.gateway.enums;

public enum DepthStepEnum {

  /**
   * 无聚合
   */
  STEP0(0, "s0"),
  /**
   * 聚合度为报价精度*10
   */
  STEP1(1, "s1"),
  /**
   * 聚合度为报价精度*100
   */
  STEP2(2, "s2"),
  /**
   * 聚合度为报价精度*1000
   */
  STEP3(3, "s3"),
  /**
   * 聚合度为报价精度*10000
   */
  STEP4(4, "s4"),
  /**
   * 聚合度为报价精度*100000
   */
  STEP5(5, "s5"),
  /**
   *
   */
  PERCENT10(10, "p10");

  public final int number;

  public final String value;

  public final String pushAddress;

  public final String subAddress;

  DepthStepEnum(int number, String value) {
    this.number = number;
    this.value = value;
    this.pushAddress = value + ".mbp.push";
    this.subAddress = value + ".mbp.sub";
  }

  public static DepthStepEnum getByName(String name) {
    for (DepthStepEnum depth : DepthStepEnum.values()) {
      if (depth.value.toLowerCase().equals(name)) {
        return depth;
      }
    }
    return null;
  }

  public static DepthStepEnum getByNumber(int number) {
    for (DepthStepEnum depth : DepthStepEnum.values()) {
      if (depth.number == number) {
        return depth;
      }
    }
    return null;
  }

}

