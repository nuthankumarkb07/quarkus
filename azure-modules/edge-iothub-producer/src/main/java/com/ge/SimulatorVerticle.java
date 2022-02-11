package com.ge;

import io.quarkus.runtime.Quarkus;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.Message;
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.IotHubEventCallback;
import com.microsoft.azure.iothub.IotHubMessageResult;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
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
            try {
                client = new DeviceClient(connString, protocol);
                client.open();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
          
            MessageSender sender = new MessageSender();
          
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(sender);
            Quarkus.waitForExit();
            executor.shutdownNow();
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
