package com.huobi.client;

import java.net.URI;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.huobi.client.exception.HuobiApiException;

/**
 * The configuration for the subscription APIs
 */
@Slf4j
@NoArgsConstructor
public class SubscribeOption {

  @Getter
  private String uri = "ws://huobi-gateway.test-12.huobiapps.com/";

  /**
   * When the connection lost is happening on the subscription line, specify whether the client reconnect to server automatically.
   * <p>The connection lost means:
   * <ul>
   * <li>Caused by network problem</li>
   * <li>The connection close triggered by server (happened every 24 hours)</li>
   * <li>No any message can be received from server within a specified time, see {@link
   * #setReceiveLimitMs(int)} (int)}</li>
   * </ul>
   *
   *  isAutoReconnect The boolean flag, true for enable, false for disable
   */
  @Setter
  @Getter
  private boolean isAutoReconnect = true;
  /**
   * the receive limit in millisecond. If no message is received within this limit time, the connection will be disconnected.
   */
  @Setter
  @Getter
  private int receiveLimitMs = 60_000;

  /**
   * If auto reconnect is enabled, specify the delay time before reconnect.
   */
  @Setter
  @Getter
  private int connectionDelayOnFailure = 15;


  public SubscribeOption(
      SubscribeOption options) {
    this.uri = options.uri;
    this.isAutoReconnect = options.isAutoReconnect;
    this.receiveLimitMs = options.receiveLimitMs;
    this.connectionDelayOnFailure = options.connectionDelayOnFailure;
  }

  /**
   * Set the URI for subscription.
   *
   * @param uri The URI name like "wss://api.huobi.pro".
   */
  public void setUri(String uri) {
    try {
      URI u = new URI(uri);
    } catch (Exception e) {
      throw new HuobiApiException(
          HuobiApiException.INPUT_ERROR, "The URI is incorrect: " + e.getMessage());
    }
    this.uri = uri;
  }
}
