package com.huobi.client;

import java.net.URL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.exception.HuobiApiException;

/**
 * The configuration for the request APIs
 */
@Slf4j
@NoArgsConstructor
public class RequestOptions {

  @Getter
  private String url = "https://api.huobi.pro";

  public RequestOptions(RequestOptions option) {
    this.url = option.url;
  }

  /**
   * Set the URL for request.
   *
   * @param url The URL name like "https://api.huobi.pro".
   */
  public void setUrl(String url) {
    try {
      URL u = new URL(url);
    } catch (Exception e) {
      throw new HuobiApiException(
          HuobiApiException.INPUT_ERROR, "The URI is incorrect: " + e.getMessage());
    }
    this.url = url;
  }

}
