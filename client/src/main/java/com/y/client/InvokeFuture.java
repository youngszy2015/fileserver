package com.y.client;

import io.netty.util.AttributeKey;

import java.util.concurrent.CountDownLatch;

public class InvokeFuture {

    public static AttributeKey<InvokeFuture> INVOKE_FUTURE = AttributeKey.valueOf("INVOKE_FUTURE");

    private Object result;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public Object waitResponse() throws InterruptedException {
        countDownLatch.await();
        return result;
    }


    public void putResponse(Object result) {
        this.result = result;
        countDownLatch.countDown();
    }

    public Object getResult() {
        return result;
    }
}
