package com.ge;

import java.time.Duration;

import com.microsoft.azure.eventhubs.RetryPolicy;

public class IotRetryPolicy extends RetryPolicy {

    protected IotRetryPolicy(String name) {
        super(name);
    }

    @Override
    protected Duration onGetNextRetryInterval(String clientId, Exception lastException, Duration remainingTime,
            int baseWaitTime) {
        return null;
    }
    
}
