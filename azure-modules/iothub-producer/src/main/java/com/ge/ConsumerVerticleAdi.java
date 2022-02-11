package com.ge;

import com.azure.core.amqp.AmqpTransportType;
import com.azure.core.amqp.ProxyAuthenticationType;
import com.azure.core.amqp.ProxyOptions;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.microsoft.azure.eventhubs.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Instant;
import java.util.function.*;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class ConsumerVerticleAdi extends AbstractVerticle {
    // private String connStr =
    // "Endpoint=sb://ihsuprodpnres007dednamespace.servicebus.windows.net/;SharedAccessKeyName=iothubowner;SharedAccessKey=D0V1VD4nX/c1hDvYu1If9O/JYivPrXeA5LFdFlC/YQY=;EntityPath=iothub-ehub-spidey-iot-17309678-034bba6000;";
    private static final Logger logger = LoggerFactory.getLogger(ConsumerVerticle.class);

    private static final String EH_COMPATIBLE_CONNECTION_STRING_FORMAT = "Endpoint=%s/;EntityPath=%s;"
            + "SharedAccessKeyName=%s;SharedAccessKey=%s";

    // az iot hub show --query properties.eventHubEndpoints.events.endpoint --name
    // {your IoT Hub name}
    private static final String EVENT_HUBS_COMPATIBLE_ENDPOINT = "sb://iothub-ns-pythongate-16825588-e9903a966d.servicebus.windows.net/";

    // az iot hub show --query properties.eventHubEndpoints.events.path --name {your
    // IoT Hub name}
    private static final String EVENT_HUBS_COMPATIBLE_PATH = "pythongateway";

    // az iot hub policy show --name service --query primaryKey --hub-name {your IoT
    // Hub name}
    private static final String IOT_HUB_SAS_KEY = "oTkbizNC5B6ai0eCQxgaOl8g9yTfVHhLwzhDaJLHcE0=";
    private static final String IOT_HUB_SAS_KEY_NAME = "service";

    private static int currMsgIndex;
    private static int prevMsgIndex;
    private static int msgDropCount;
    private static int recMsgCount;

    private static FileWriter myFile;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.setPeriodic(1000, x -> {
            try {
                // Build the Event Hubs compatible connection string.
                String eventHubCompatibleConnectionString = String.format(EH_COMPATIBLE_CONNECTION_STRING_FORMAT,
                        EVENT_HUBS_COMPATIBLE_ENDPOINT, EVENT_HUBS_COMPATIBLE_PATH, IOT_HUB_SAS_KEY_NAME,
                        IOT_HUB_SAS_KEY);

                myFile = new FileWriter("DropMsgLogs-" + Math.random() + ".txt");
                // Setup the EventHubBuilder by configuring various options as needed.
                EventHubClientBuilder eventHubClientBuilder = null;
                try {
                    eventHubClientBuilder = new EventHubClientBuilder()
                            .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                            .connectionString(eventHubCompatibleConnectionString);
                } catch (Exception e) {
                    System.err.println("Error occured " + e.getMessage());
                }

                // setupProxy(eventHubClientBuilder);
                // eventHubClientBuilder.transportType(AmqpTransportType.AMQP_WEB_SOCKETS);

                // Create an async consumer client as configured in the builder.
                try (EventHubConsumerAsyncClient eventHubConsumerAsyncClient = eventHubClientBuilder
                        .buildAsyncConsumerClient()) {

                    receiveFromAllPartitions(eventHubConsumerAsyncClient);

                    System.out.println("Press ENTER to exit.");
                    System.in.read();

                    System.out.println("Shutting down...\nMessage Drop Count..."
                            + msgDropCount
                            + "\nTotal received messages..."
                            + recMsgCount);
                    myFile.close();
                }
            } catch (IOException e) {
                logger.info("Failed to receive");
                e.printStackTrace();
            }
        });
    }

    private static void receiveFromAllPartitions(EventHubConsumerAsyncClient eventHubConsumerAsyncClient) {

        eventHubConsumerAsyncClient
                .receive(false) // set this to false to read only the newly available events
                .subscribe(partitionEvent -> {
                    try {

                        System.out.println();
                        System.out.printf("%nCPN Messages received from partition %s:%n%s",
                                partitionEvent.getPartitionContext().getPartitionId(),
                                partitionEvent.getData().getBodyAsString());
                        System.out.printf("%nApplication properties (set by device):%n%s",
                                partitionEvent.getData().getProperties());
                        System.out.printf("%nSystem properties (set by IoT Hub):%n%s",
                                partitionEvent.getData().getSystemProperties());
                        // obj.checkMsgDrop(partitionEvent.getData().getProperties().get("messageIndex").toString());
                        // obj.checkLatency(partitionEvent.getData().getProperties().get("messageSentTime").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    System.err.println("Error occurred while consuming events: "
                            + error.getMessage()
                            + "\n" + error);
                }, () -> {
                    System.out.println("Completed receiving events");
                });

    }

    /**
     * This method checks for latency values.
     * 
     * @param string
     */
    private void checkLatency(String string) {
        synchronized (this) {
            try {
                long creationTime = Long.parseLong(string);
                long receivedTime = Instant.now().toEpochMilli();
                System.out.println("Latency in ms " + (receivedTime - creationTime));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * This method checks for Message drop by comparing previous and current message
     * index.
     * In case of message drop, the indexes are written to a log File.
     * 
     * @param string
     */

    private void checkMsgDrop(String string) {
        synchronized (this) {
            currMsgIndex = Integer.parseInt(string);
            try {
                if (currMsgIndex == (prevMsgIndex + 1) || prevMsgIndex == 0) {
                    recMsgCount++;
                    System.out.println("\nRunning Message Counter " + recMsgCount);
                } else {
                    msgDropCount++;
                    System.err.println("Error: Msg Drop since index diff -> " + (currMsgIndex - prevMsgIndex));
                    logIndexToFile("\n\nPrev Index " + prevMsgIndex + "\nCurr Index " + currMsgIndex);
                }
                prevMsgIndex = currMsgIndex;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Append indexes to log file when message drops or messages are out of order.
     * 
     * @param str
     * @throws IOException
     */
    private static void logIndexToFile(String str) throws IOException {
        myFile.append(str);
    }

    /**
     * This method sets up proxy options and updates the
     * {@link EventHubClientBuilder}.
     *
     * @param eventHubClientBuilder The {@link EventHubClientBuilder}.
     */
    private static void setupProxy(EventHubClientBuilder eventHubClientBuilder) {
        int proxyPort = 80; // replace with right proxy port
        String proxyHost = "proxy-privzen.jfwtc.ge.com";
        Proxy proxyAddress = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        String userName = null;
        String password = null;
        ProxyOptions proxyOptions = new ProxyOptions(ProxyAuthenticationType.BASIC, proxyAddress,
                userName, password);

        eventHubClientBuilder.proxyOptions(proxyOptions);

        // To use proxy, the transport type has to be Web Sockets.
        eventHubClientBuilder.transportType(AmqpTransportType.AMQP_WEB_SOCKETS);
    }
}
