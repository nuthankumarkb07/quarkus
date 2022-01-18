package com.ge;

import java.util.Random;
import java.util.UUID;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class SimulatorVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SimulatorVerticle.class);
    private double temp = 21.0;
    private final Random rand = new Random();
    private NetClient client = null;
    private NetSocket socket =null;
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.setPeriodic(1000, x -> {
            if (client == null) {
            io.vertx.core.net.NetClient tcpClient= vertx.createNetClient().getDelegate();
            client = tcpClient.connect(TCP_PORT, "localhost", "simulator",
                    new Handler<AsyncResult<NetSocket>>() {
                        @Override
                        public void handle(AsyncResult<NetSocket> result) {
                             socket = result.result();
                        }
                    });
                }
            this.writetosocket(socket);
        });
    }
    private void writetosocket(NetSocket socket){
        JsonObject json = new JsonObject();
        json.put("uuid", UUID.randomUUID().toString());
        json.put("time", System.currentTimeMillis());
        json.put("temp", temp + (rand.nextGaussian() / 2.0d));
        json.put("caller", socket.remoteAddress().toString());
        json.encodePrettily();
        System.out.println("writing to socker"+json.toString());
        socket.write(json.toString());
    }
}
