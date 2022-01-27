package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.verticle.ConnectorVerticle;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@Startup
@QuarkusMain
@ApplicationScoped
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(MyApp.class);
@Inject
static ConnectorVerticle myVerticle;
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }
    public static class MyApp implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            Vertx vertx = Vertx.vertx();
            try{
                //Verticle myVerticle = new ConnectorVerticle();
                vertx.deployVerticle(myVerticle);
                logger.info("Verticle deployment successful");
            }catch(Exception e){
                logger.info("Verticle deployment failed");
            }         
            Quarkus.waitForExit();
            return 0;
        }
    }
}