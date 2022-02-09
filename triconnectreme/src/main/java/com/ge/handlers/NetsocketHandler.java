package com.ge.handlers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

@ApplicationScoped
public class NetsocketHandler implements Handler<NetSocket> {
    @Inject PayloadHandler payloadHandler;
    @Override
    public void handle(NetSocket event) {
        event.handler(payloadHandler);
    }

}
