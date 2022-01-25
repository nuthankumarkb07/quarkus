package com.ge;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

@QuarkusMain
@ApplicationScoped
public class Main {

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }
    public static class MyApp implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            Vertx vertx = Vertx.vertx();
            Verticle myVerticle = new ConnectorVerticle();
            vertx.deployVerticle(myVerticle);
            Quarkus.waitForExit();
            return 0;
        }
    }
}