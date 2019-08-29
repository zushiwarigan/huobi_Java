package com.huobi.client.v1.utils;

import com.huobi.client.v1.AsyncResult;
import com.huobi.client.v1.exception.HuobiApiException;

public class FailedAsyncResult<T> implements AsyncResult<T> {

  private final HuobiApiException exception;

  public FailedAsyncResult(HuobiApiException exception) {
    this.exception = exception;
  }

  @Override
  public HuobiApiException getException() {
    return exception;
  }

  @Override
  public boolean succeeded() {
    return false;
  }

  @Override
  public T getData() {
    return null;
  }
}
