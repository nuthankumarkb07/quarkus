package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import com.ge.debugger.BeanExplorer;
import com.ge.vertx.VertxBean;
import com.ge.vertx.receiverVerticle;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@Startup
@ApplicationScoped
public class App<T> {
    @Inject VertxBean<T> vertx;
    @Inject BeanManager beanManager;
    @Inject BeanExplorer beanExplorer;
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    void onStart(@Observes StartupEvent ev) {
        vertx.startVertx();
        beanExplorer.getbeans(beanManager);
        logger.info("The application has started");
    }
    void onStop(@Observes ShutdownEvent ev) {
        vertx.stopVertx();
        logger.info("The application is stopping...");
    }
}