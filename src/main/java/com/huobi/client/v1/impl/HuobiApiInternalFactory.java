package com.huobi.client.v1.impl;

import java.net.URI;

import com.huobi.client.v1.AsyncRequestClient;
import com.huobi.client.v1.RequestOptions;
import com.huobi.client.v1.SubscribeClient;
import com.huobi.client.v1.SubscribeOption;
import com.huobi.client.v1.SubscriptionClient;
import com.huobi.client.v1.SubscriptionOptions;
import com.huobi.client.v1.SyncRequestClient;

public final class HuobiApiInternalFactory {

  private static final HuobiApiInternalFactory instance = new HuobiApiInternalFactory();

  public static HuobiApiInternalFactory getInstance() {
    return instance;
  }

  private HuobiApiInternalFactory() {
  }

  public SyncRequestClient createSyncRequestClient(
      String apiKey, String secretKey, RequestOptions options) {
    RequestOptions requestOptions = new RequestOptions(options);
    RestApiRequestImpl requestImpl = new RestApiRequestImpl(apiKey, secretKey, requestOptions);
    if (!"".equals(apiKey) && !"".equals(secretKey)) {
      AccountsInfoMap.updateUserInfo(apiKey, requestImpl);
    }
    return new SyncRequestImpl(requestImpl);
  }

  public AsyncRequestClient createAsyncRequestClient(
      String apiKey, String secretKey, RequestOptions options) {
    RequestOptions requestOptions = new RequestOptions(options);
    RestApiRequestImpl requestImpl = new RestApiRequestImpl(apiKey, secretKey, requestOptions);
    if (!"".equals(apiKey) && !"".equals(secretKey)) {
      AccountsInfoMap.updateUserInfo(apiKey, requestImpl);
    }
    return new AsyncRequestImpl(requestImpl);
  }

  public SubscriptionClient createSubscriptionClient(
      String apiKey, String secretKey, SubscriptionOptions options) {
    SubscriptionOptions subscriptionOptions = new SubscriptionOptions(options);
    RequestOptions requestOptions = new RequestOptions();
    try {
      String host = new URI(options.getUri()).getHost();
      requestOptions.setUrl("https://" + host);
    } catch (Exception e) {

    }
    RestApiRequestImpl requestImpl = new RestApiRequestImpl(apiKey, secretKey, requestOptions);
    SubscriptionClient webSocketStreamClient = new WebSocketStreamClientImpl(
        apiKey, secretKey, subscriptionOptions);
    if (!"".equals(apiKey) && !"".equals(secretKey)) {
      AccountsInfoMap.updateUserInfo(apiKey, requestImpl);
    }
    return webSocketStreamClient;
  }

  public SubscribeClient createSubscribeClient(
      String apiKey, String secretKey, SubscribeOption options) {
    SubscribeOption subscribeOption = new SubscribeOption(options);
    RequestOptions requestOptions = new RequestOptions();
    try {
      String host = new URI(options.getUri()).getHost();
      requestOptions.setUrl("https://" + host);
    } catch (Exception e) {

    }
    RestApiRequestImpl requestImpl = new RestApiRequestImpl(apiKey, secretKey, requestOptions);
    SubscribeClient wsStreamClient = new WSStreamClientImpl(
        apiKey, secretKey, subscribeOption);
    if (!"".equals(apiKey) && !"".equals(secretKey)) {
      AccountsInfoMap.updateUserInfo(apiKey, requestImpl);
    }
    return wsStreamClient;
  }

}
