package com.ge.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.ge.helpers.Payload;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
@Singleton
public class PayloadHandler<T> implements Handler<Buffer> {
    @Inject
    Payload<T> payload;

    @Override
    public void handle(Buffer event) {
        payload.receiveJsonData(event.toJsonObject());
    }
}
