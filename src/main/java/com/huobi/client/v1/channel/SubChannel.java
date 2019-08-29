package com.huobi.client.v1.channel;


import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubChannel {
  String action;

  String ch;


  public String toJSONString(){
    return JSON.toJSONString(this);
  }
}
