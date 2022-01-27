package com.pojo;

import javax.inject.Inject;
import io.vertx.core.Vertx;
public class VertxResource {

    public Vertx vertx;
    @Inject
    public VertxResource(Vertx vertx) {
        this.vertx = vertx;
    }
}