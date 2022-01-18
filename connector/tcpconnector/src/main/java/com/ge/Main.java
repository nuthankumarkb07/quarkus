package com.ge;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("TCP_PORT", "8080"));
    public static void main (String[] args){
        Vertx vertx = Vertx.vertx();
            NetServer server = vertx.createNetServer();
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
