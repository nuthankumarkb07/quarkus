package com.ge;

import java.time.Instant;

import com.microsoft.azure.eventhubs.EventPosition;

public class IotEventPosition implements EventPosition{

    @Override
    public Long getSequenceNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instant getEnqueuedTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOffset() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getInclusiveFlag() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
