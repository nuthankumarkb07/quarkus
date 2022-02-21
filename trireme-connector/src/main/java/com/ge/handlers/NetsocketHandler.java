package com.ge.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

@Singleton
public class NetsocketHandler<T> implements Handler<NetSocket> {
    @Inject PayloadHandler<T> payloadHandler;
    @Override
    public void handle(NetSocket event) {
        event.handler(payloadHandler);
    }

}
