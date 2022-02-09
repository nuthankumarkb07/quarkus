package com.ge.helpers;

import java.sql.Time;
import java.time.Instant;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ge.debugger.BeanExplorer;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@ApplicationScoped
public class Payload {
    private JsonObject receivedjsonobject;
    @Inject
    EventBus eventbus;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);

    public void receiveJsonData(JsonObject event) {
        try {
            //Transform the object the way it is needed.
            this.receivedjsonobject = event;
            this.receivedjsonobject.put("receivedtimestamp", Time.from(Instant.now()).toString());
            logger.info(this.receivedjsonobject.toString());
        } catch (Exception e) {
            System.out.println("error receiving data");
            e.printStackTrace();
        }
    }
}
