package com.ge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import com.microsoft.azure.iot.service.exceptions.IotHubException;
import com.microsoft.azure.iot.service.sdk.Device;
import com.microsoft.azure.iot.service.sdk.RegistryManager;
import java.io.IOException;
import java.net.URISyntaxException;

@Startup
@ApplicationScoped
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String connectionString = "HostName=spidey-iothub.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=D0V1VD4nX/c1hDvYu1If9O/JYivPrXeA5LFdFlC/YQY=";
    private static final String deviceId = "spidey";

    void onStart(@Observes StartupEvent ev) throws IOException, URISyntaxException, Exception{
        RegistryManager registryManager = RegistryManager.createFromConnectionString(connectionString);
        Device device = Device.createFromId(deviceId, null, null);
        try {
            device = registryManager.addDevice(device);
        } catch (IotHubException iote) {
            try {
                device = registryManager.getDevice(deviceId);
            } catch (IotHubException iotf) {
                iotf.printStackTrace();
            }
        }
        logger.info("Device id: " + device.getDeviceId());
        logger.info("Device key: " + device.getPrimaryKey());
        logger.info("The application has started");
        //Vertx vertx =Vertx.vertx(new VertxOptions().setBlockedThreadCheckInterval(30000));
        Vertx vertx =Vertx.vertx();
        vertx.deployVerticle(new ConsumerVerticle());
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
    }
}