package com.ge.vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ge.handlers.NetsocketHandler;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@ApplicationScoped
public class VertxBean {
    private static final int TCP_PORT = Integer.parseInt(System.getenv().getOrDefault("TCP_PORT", "9999"));
    private static final Logger logger = LoggerFactory.getLogger(VertxBean.class);
    private static NetServer server = null;
    @Inject
    Vertx vertx;
    @Inject
    NetsocketHandler netsocketHandler;

    public void startVertx() {
        server = vertx.createNetServer();
        server.connectHandler(netsocketHandler);
        server.listen(TCP_PORT);
    }

    public void stopVertx() {
        vertx.close();
        logger.info("Stop everything before we need to exit the applications");
    }

}