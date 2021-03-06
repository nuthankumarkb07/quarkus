package com.ge;

import io.quarkus.runtime.Quarkus;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class SimulatorVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SimulatorVerticle.class);
    private static String connString = "HostName=spidey-iothub.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=D0V1VD4nX/c1hDvYu1If9O/JYivPrXeA5LFdFlC/YQY=";
    private static IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;
    private static DeviceClient client;

    @Override
    public void start(Promise<Void> startPromise) throws IOException, URISyntaxException, Exception{
        vertx.setPeriodic(1000, x -> {

        });
    }


}
