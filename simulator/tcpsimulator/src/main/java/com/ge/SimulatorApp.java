package com.ge;

import io.vertx.core.Vertx;

public class SimulatorApp {
    public static void main(String[] args) {
        Vertx vertx =Vertx.vertx();
        vertx.deployVerticle(new SimulatorVerticle());
    }
}
