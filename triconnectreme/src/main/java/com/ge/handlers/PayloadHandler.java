package com.ge.handlers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ge.helpers.Payload;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

@ApplicationScoped
public class PayloadHandler implements Handler<Buffer> {
    @Inject
    Payload payload;

    @Override
    public void handle(Buffer event) {
        payload.capture(event.toJson());
    }

}
