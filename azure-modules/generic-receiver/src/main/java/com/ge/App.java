package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@Startup
@ApplicationScoped
public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    void onStart(@Observes StartupEvent ev) {
        logger.info("The application has started");
    }
    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
    }
}