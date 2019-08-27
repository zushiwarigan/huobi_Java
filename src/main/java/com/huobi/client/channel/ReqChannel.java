package com.huobi.client.channel;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReqChannel {

  private long seq;

  private String action;

  private String ch;

  private Map<String,Object> params = new HashMap<>();

}
