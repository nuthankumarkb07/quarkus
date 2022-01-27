package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import io.vertx.core.Vertx;

@ApplicationScoped
public class VertxBean {
    @Inject Vertx vertx;
    
    @Inject ConnectorVerticle myVerticle;
    public void startVertx(){
        vertx.deployVerticle(myVerticle);
    }
    public void  stopVertx(){
        vertx.close();
        System.out.println("Stop everything before we need to exit the applications");
    }

}