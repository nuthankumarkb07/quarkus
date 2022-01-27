package com.ge;

import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class ConnectorApp {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorApp.class);
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("TCP_PORT", "8080"));
    public static void main (String[] args){
        Vertx vertx =Vertx.vertx();
        vertx.deployVerticle(new ConnectorVerticle());
    }
}
