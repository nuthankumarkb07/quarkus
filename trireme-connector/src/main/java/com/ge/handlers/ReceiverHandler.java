package com.ge.handlers;

import javax.inject.Singleton;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

@Singleton
public class ReceiverHandler<T> implements Handler<Message<T>> {

    @Override
    public void handle(Message<T> event) {
        System.out.println(event.body()); 
        
    }

}
