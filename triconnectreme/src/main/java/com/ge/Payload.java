package com.ge;

// import java.io.File;
// import java.io.FileInputStream;
// import java.security.Timestamp;
// import java.security.cert.CertPath;
// import java.security.cert.Certificate;
// import java.security.cert.CertificateFactory;
import java.sql.Time;
import java.time.Instant;
// import java.util.Collections;
// import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

@ApplicationScoped
public class Payload{
    private JsonObject receivedjsonobject;
    private JsonObject sendingjsonobject;
    private static final Logger logger = LoggerFactory.getLogger(BeanExplorer.class);
    
    public void capture(Object object) {
        try {
            // File f = new File(System.getProperty("test.src", "."), "cert_file");
            // FileInputStream fis = new FileInputStream(f);
            // CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // Certificate c = cf.generateCertificate(fis);
            // CertPath cp = cf.generateCertPath(Collections.singletonList(c));
            // fis.close();
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
