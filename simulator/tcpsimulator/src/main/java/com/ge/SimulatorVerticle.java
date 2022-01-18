package com.ge;

import java.util.Random;

import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;

public class SimulatorVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SimulatorVerticle.class);
    private double temp = 21.0;
    private final Random rand = new Random();
    private static final int HTTP_PORT = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));
    // @Override
    // public void start(){
    // io.vertx.rx.http.HttpClient httpClient = vertx.createHttpClient();
    // httpClient.getNow(8080, "localhost", "/", new Handler<HttpClientResponse>() {

    // @Override
    // public void handle(HttpClientResponse httpClientResponse) {
    // System.out.println("Response received");
    // }
    // });
    // }
    @Override
    public void start() {
        io.vertx.mutiny.core.net.NetClient tcpClient = vertx.createNetClient();
        io.vertx.core.net.NetClient tcpClient1 = vertx.createNetClient().getDelegate();
        // tcpClient.connect(8080, "localhost","nutsy");
        tcpClient1.connect(8080, "localhost", "nutsy",
                new Handler<AsyncResult<NetSocket>>() {
                    @Override
                    public void handle(AsyncResult<NetSocket> result) {
                        NetSocket socket = result.result();
                        for (int i = 0; i < 1000; i++) {
                            socket.write("socket data");
                            
                        }
                    }
                });
        vertx.setPeriodic(2000, x -> {
            this.updatetemp();
            System.out.println(temp);
        });
    }

    private void updatetemp() {
        temp = temp + (rand.nextGaussian() / 2.0d);
    }
}
