package com.huobi.client.v2.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubChannel {

  private String ch;

  private String action;

  public String toJSONString(){
    return JSON.toJSONString(this);
  }

}
