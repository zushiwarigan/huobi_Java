package com.huobi.client.v1;

import com.huobi.client.v1.exception.HuobiApiException;

/**
 * The error handler for the subscription.
 */
@FunctionalInterface
public interface SubscriptionErrorHandler {

  void onError(HuobiApiException exception);
}
