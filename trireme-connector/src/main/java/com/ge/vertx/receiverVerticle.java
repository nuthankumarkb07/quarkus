package com.ge.vertx;

import javax.inject.Inject;

import com.ge.handlers.ReceiverHandler;

import io.vertx.core.AbstractVerticle;
public class receiverVerticle<T> extends AbstractVerticle {
    @Inject ReceiverHandler<T> receiver;
    @Override
    public void start() throws Exception {
        super.start();
       // vertx.eventBus().consumer("receiver1",receiver);
        vertx.eventBus().consumer("receiver1", message -> {
             System.out.println("receiver1 " + message.body());
         });
        vertx.eventBus().consumer("receiver2", message -> {
            System.out.println("receiver2 " + message.body());
        });
        vertx.eventBus().consumer("receiver3", message -> {
            System.out.println("receiver3 " + message.body());
        });
        
        
    }

}