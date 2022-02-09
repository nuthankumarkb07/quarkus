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
    private JsonObject sendingjsonobject;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);

    public void capture(Object object) {
        try {
            this.receivedjsonobject = (JsonObject) object;
            JsonObject result = this.receivedjsonobject;
            result.put("receivedtimestamp", Time.from(Instant.now()).toString());
            this.sendingjsonobject = result;
            logger.info(sendingjsonobject.toString());
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }
}
