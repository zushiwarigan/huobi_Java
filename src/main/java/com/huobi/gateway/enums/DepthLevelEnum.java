package com.huobi.gateway.enums;

/**
 * 数据级别
 */
public enum DepthLevelEnum {

  /**
   * 5条
   */
  LEVEL_5(5),

  /**
   * 10条
   */
  LEVEL_10(10),

  /**
   * 20条
   */
  LEVEL_20(20),

  /**
   * 50
   */
  LEVEL_50(50),

  /**
   * 100
   */
  LEVEL_100(100);

  public final int level;

  DepthLevelEnum(int level) {
    this.level = level;
  }

  public static DepthLevelEnum getByLevel(int level) {
    for (DepthLevelEnum e : DepthLevelEnum.values()) {
      if (e.level == level) {
        return e;
      }
    }
    return null;
  }

  public static int getMaxLevel() {
    int max = 0;
    for (DepthLevelEnum value : values()) {
      if (value.level > max) {
        max = value.level;
      }
    }
    return max;
  }
}


