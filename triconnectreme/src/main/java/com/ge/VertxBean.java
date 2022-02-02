package com.ge;


import java.security.Timestamp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@ApplicationScoped
public class VertxBean {
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("TCP_PORT", "9999"));
    private static final Logger logger = LoggerFactory.getLogger(VertxBean.class);
    private static NetServer server = null;
    @Inject Vertx vertx;
    @Inject Payload payload;
    public void startVertx(){
        server = vertx.createNetServer();
        server.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket netSocket) {
                logger.info("Incoming connection!");
                netSocket.handler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        payload.capture(buffer.toJson());
                    }
                });
            }
        });
        server.listen(TCP_PORT);
    }
    public void  stopVertx(){
        vertx.close();
        logger.info("Stop everything before we need to exit the applications");
    }

}