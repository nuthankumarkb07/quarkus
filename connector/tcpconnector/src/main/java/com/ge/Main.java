package com.ge;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Router;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main (String[] args){
        Vertx vertx = Vertx.vertx();
            NetServer server = vertx.createNetServer();
            server.connectHandler(new Handler<NetSocket>() {

                @Override
                public void handle(NetSocket netSocket) {
                    System.out.println("Incoming connection!");
                }
            });
            server.listen(8080);
            
        



        // Vertx vertx = Vertx.vertx();
        // Router router = Router.router(vertx);
        // router.get("/").handler(rc -> rc.response().end("Hello"));
        // //router.get("/").handler(rc -> rc.response().end("Hello")).respond(rc -> Future.succeededFuture(new Pojo()));
        
        // //Object json = (Object) router.get("/").handler(rc -> rc.response().end("Hello")).respond(rc -> Future.succeededFuture(new JsonObject().put("hello", "world")));
        // //System.out.println(json.toString());
        
        // //router.get("/:name").handler(rc -> rc.response().end("Hello " + rc.pathParam("name")));
        // vertx.createHttpServer()
        //         .requestHandler(router)
        //         .listen(8080).onSuccess(ok -> {
        //             System.out.println("success");
        //         }).onFailure(ok -> {
        //             System.out.print("failed");
        //         });
    }
}
