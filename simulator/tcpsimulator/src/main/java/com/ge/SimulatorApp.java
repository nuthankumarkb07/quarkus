package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import io.vertx.core.Vertx;
@Startup
@ApplicationScoped
public class SimulatorApp {
    private static final Logger logger = LoggerFactory.getLogger(SimulatorApp.class);
    
    void onStart(@Observes StartupEvent ev) {
        Vertx vertx =Vertx.vertx();
        vertx.deployVerticle(new SimulatorVerticle());
    }
    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The Simulator is stopping...");
    }
}