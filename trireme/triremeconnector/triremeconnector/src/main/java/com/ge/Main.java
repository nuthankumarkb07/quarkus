package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Verticle;
import javax.enterprise.inject.Instance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

@QuarkusMain
@ApplicationScoped
public class Main {

    @Inject
    Vertx vertx;

    public void init(@Observes StartupEvent e, Vertx vertx, Instance<AbstractVerticle> verticles) {
        for (AbstractVerticle verticle : verticles) {
            vertx.deployVerticle(verticle);
        }
    }

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            System.out.println("Do startup logic here");
            Vertx vertx = Vertx.vertx();
            Verticle myVerticle = new ConnectorVerticle();
            vertx.deployVerticle(myVerticle);
            Quarkus.waitForExit();
            return 0;
        }
    }
}