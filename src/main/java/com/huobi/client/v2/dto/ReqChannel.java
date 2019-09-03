package com.huobi.client.v2.dto;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqChannel {

  private long seq;

  private String action;

  private String ch;

  private Map<String, Object> params = new HashMap<>();

  public String toJSONString() {
    return JSON.toJSONString(this);
  }
}

