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
public class Payload<T> {
    private JsonObject receivedjsonobject;
    @Inject
    EventBus eventbus;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);
    private int count = 0;

    public void receiveJsonData(JsonObject event) {
        try {
            this.receivedjsonobject = event;
            this.receivedjsonobject.put("receivedtimestamp", Time.from(Instant.now()).toString());
            switch (count) {
                case 0:
                    eventbus.publish("receiver1", receivedjsonobject);
                    count++;
                    break;
                case 1:
                    eventbus.publish("receiver2", receivedjsonobject);
                    count++;
                    break;
                case 2:
                    eventbus.publish("receiver3", receivedjsonobject);
                    count =0;
                    break;
                default:
                    logger.info("Error occured while distribution of messages."+count);
                    break;
            }
        } catch (Exception e) {
            System.out.println("error receiving data");
            e.printStackTrace();
        }
    }
}
