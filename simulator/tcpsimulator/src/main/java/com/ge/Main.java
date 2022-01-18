package com.ge;

import io.vertx.core.Vertx;

public class Main {
    // private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Vertx vertx =Vertx.vertx();
        vertx.deployVerticle(new SimulatorVerticle());
    }
}
