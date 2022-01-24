package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;

import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
@Any
@ApplicationScoped
public class ConnectorVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorVerticle.class);
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("TCP_PORT", "9999"));
    private static NetServer server = null;
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("starting connector");
        if(server==null){
        Vertx vertx = Vertx.vertx();
        server = vertx.createNetServer();
        }
        server.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket netSocket) {
                System.out.println("Incoming connection!");
                netSocket.handler(new Handler<Buffer>(){
                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("Received data: " + buffer.length());
                        System.out.println(buffer.getString(0, buffer.length()));
                    }
                });
            }
        });
        server.listen(TCP_PORT);
    }
}
