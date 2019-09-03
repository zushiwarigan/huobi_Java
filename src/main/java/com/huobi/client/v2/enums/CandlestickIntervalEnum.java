package com.huobi.client.v2.enums;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.huobi.client.v2.constant.Const;


public enum CandlestickIntervalEnum {

  /**
   * 一分钟
   */
  MIN_1(0, "1m", "1min", 1, TimeUnit.MINUTES),
  /**
   * 5分钟
   */
  MIN_5(1, "5m", "5min", 5, TimeUnit.MINUTES),
  /**
   * 15分钟
   */
  MIN_15(2, "15m", "15min", 15, TimeUnit.MINUTES),
  /**
   * 30分钟
   */
  MIN_30(3, "30m", "30min", 30, TimeUnit.MINUTES),
  /**
   * 60分钟
   */
  MIN_60(4, "60m", "60min", 1, TimeUnit.HOURS),
  /**
   * 4小时
   */
  HOUR_4(5, "4h", "4hour", 4, TimeUnit.HOURS),
  /**
   * 一天
   */
  DAY_1(6, "1d", "1day", 1, TimeUnit.DAYS),
  /**
   * 一周
   */
  WEEK_1(7, "1w", "1week", 7, TimeUnit.DAYS),
  /**
   * 一月
   */
  MON_1(8, "1M", "1mon", 1, null),
  /**
   * 一年
   */
  YEAR_1(9, "1y", "1year", 1, null);


  CandlestickIntervalEnum(int code, String value, String alias, int unitValue, TimeUnit timeUnit) {
    this.code = code;
    this.value = value;
    this.alias = alias;
    if (timeUnit == null) {
      this.seconds = 0;
    } else {
      this.seconds = (int) timeUnit.toSeconds(unitValue);
    }
  }

  public final int code;
  public final String value;
  public final String alias;

  private final int seconds;


  // 从1970到2035年东8区每月一日0时的时间戳(秒)
  private static final int[] months = new int[792];

  // 从1970到2035年东8区每年一月一日0时的时间戳(秒)
  private static final int[] years = new int[66];

  // 2036-01-01 0时东8区的时间戳(秒)
  private static final int MAX_TS;

  // 东8区0时的时间戳(秒)偏移量
  private static final int DAY_OFFSET;

  // 东8区周一0时的时间戳(秒)偏移量
  private static final int WEEK_OFFSET;

  static {
    DAY_OFFSET = (int) Instant.ofEpochSecond(0)
        .atZone(Const.DEFAULT_ZONE)
        .toLocalDate()
        .atStartOfDay(Const.DEFAULT_ZONE)
        .toEpochSecond();

    LocalDate date = Instant.ofEpochSecond(0)
        .atZone(Const.DEFAULT_ZONE)
        .toLocalDate();

    if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
      date = date.plusDays(DayOfWeek.SUNDAY.ordinal() - date.getDayOfWeek().ordinal());
    }

    WEEK_OFFSET = (int) date.atStartOfDay(Const.DEFAULT_ZONE).toEpochSecond();

    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Const.DEFAULT_ZONE));
    cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);

    for (int i = 0; i < months.length; ++i) {
      int ts = (int) (cal.getTimeInMillis() / 1000);
      months[i] = ts;
      if (i % 12 == 0) {
        years[i / 12] = ts;
      }
      cal.add(Calendar.MONTH, 1);
    }

    MAX_TS = (int) (cal.getTimeInMillis() / 1000);
  }


  public int getStartTime(int seconds) {
    return getStartTime(seconds, 0);
  }

  /**
   * 获取开始时间
   *
   * @param seconds 单位秒
   */
  public int getStartTime(int seconds, int offset) {
    if (seconds < minTs()) {
      return minTs();
    }
    if (seconds > maxTs()) {
      return maxTs();
    }

    switch (this) {
      case MIN_1:
      case MIN_5:
      case MIN_15:
      case MIN_30:
      case MIN_60:
      case HOUR_4:
      case DAY_1:
      case WEEK_1:
        int periodOffset = 0;
        if (this == DAY_1) {
          periodOffset = DAY_OFFSET;
        } else if (this == WEEK_1) {
          periodOffset = WEEK_OFFSET;
        }
        int tmp = (seconds - periodOffset) / this.seconds * this.seconds;
        return (tmp + offset * this.seconds) + periodOffset;
      case MON_1:
        return startTime(seconds, offset, months);

      case YEAR_1:
        return startTime(seconds, offset, years);

      default:
        throw new IllegalArgumentException("不支持的K线类型");
    }
  }

  private int startTime(int seconds, int offset, int[] tms) {
    for (int i = tms.length - 1; i >= 0; --i) {
      if (tms[i] <= seconds) {
        int index = i + offset;
        if ((index > 0) && (index < tms.length - 1)) {
          return tms[index];
        }
      }
    }
    return minTs();
  }

  /**
   * 计算包含开始时间和结束时间的K线数量
   */
  public int count(int startTs, int endTs) {
    startTs = getStartTime(startTs);
    endTs = getStartTime(endTs);

    if (this == MON_1) {
      return Arrays.binarySearch(months, endTs) - Arrays.binarySearch(months, startTs) + 1;
    }

    if (this == YEAR_1) {
      return Arrays.binarySearch(years, endTs) - Arrays.binarySearch(years, startTs) + 1;
    }

    return (endTs - startTs) / getInterval() + 1;
  }

  /**
   * 由于时间戳使用了秒数，所以限制本系统支持的时间范围是[1970.01.01, 2036.01.01)
   */
  public static boolean validate(int ts) {
    return (ts > minTs() && ts < maxTs());
  }

  /**
   * 系统支持的最小时间戳(不小于该值)
   */
  public static int minTs() {
    return years[0];
  }

  /**
   * 系统支持的最大时间戳(小于该值)
   */
  public static int maxTs() {
    return MAX_TS;
  }

  public int getInterval() {
    return seconds;
  }

  public static CandlestickIntervalEnum getByCode(int code) {
    for (CandlestickIntervalEnum interval : CandlestickIntervalEnum.values()) {
      if (interval.code == code) {
        return interval;
      }
    }
    return null;
  }

  public static CandlestickIntervalEnum getByValue(String value) {
    for (CandlestickIntervalEnum interval : CandlestickIntervalEnum.values()) {
      if (interval.value.equals(value)) {
        return interval;
      }
    }
    return null;
  }

  public static CandlestickIntervalEnum getByAlias(String alias) {
    for (CandlestickIntervalEnum interval : CandlestickIntervalEnum.values()) {
      if (interval.alias.equals(alias)) {
        return interval;
      }
    }
    return null;
  }

}
