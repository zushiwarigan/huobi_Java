package com.huobi.client.v1.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.huobi.client.v1.utils.UrlParamsBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ApiSignature.class})
@PowerMockIgnore({"javax.crypto.*"})
public class TestApiSignature {

  @Test
  public void testSignature() {
    PowerMockito.mockStatic(ApiSignature.class);
    PowerMockito.when(ApiSignature.gmtNow()).thenReturn("123");
    ApiSignature signature = new ApiSignature();
    UrlParamsBuilder builder = UrlParamsBuilder.build();
    signature.createSignature("123", "456", "GET", "host", "url", builder);
    assertEquals("AccessKeyId=123"
            + "&Signature=Hfdw0d%2F3ZOEB2eHJZiwVZ%2F1YLL62MFuKKmSW83vM3ps%3D"
            + "&SignatureMethod=HmacSHA256"
            + "&SignatureVersion=2"
            + "&Timestamp=123",
        builder.buildSignature());
  }
}