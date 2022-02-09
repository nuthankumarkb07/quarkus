package com.ge.helpers;

import java.sql.Time;
import java.time.Instant;
import javax.enterprise.context.ApplicationScoped;

import com.ge.debugger.BeanExplorer;

import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@ApplicationScoped
public class Payload {
    private JsonObject receivedjsonobject;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);

    public void receiveJsonData(Object object) {
        try {
            //Transform the object the way it is needed.
            this.receivedjsonobject = (JsonObject) object;
            this.receivedjsonobject.put("receivedtimestamp", Time.from(Instant.now()).toString());
            logger.info(this.receivedjsonobject.toString());
        } catch (Exception e) {
            System.out.println("error receiving data");
            e.printStackTrace();
        }
    }
}
