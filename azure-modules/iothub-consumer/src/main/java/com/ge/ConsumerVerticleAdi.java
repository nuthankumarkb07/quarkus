package com.ge;

import com.azure.core.amqp.AmqpTransportType;
import com.azure.core.amqp.ProxyAuthenticationType;
import com.azure.core.amqp.ProxyOptions;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Instant;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ConsumerVerticleAdi extends AbstractVerticle {
    private static final String EH_COMPATIBLE_CONNECTION_STRING_FORMAT = "Endpoint=%s/;EntityPath=%s;"
            + "SharedAccessKeyName=%s;SharedAccessKey=%s";
    private static final String EVENT_HUBS_COMPATIBLE_ENDPOINT = "sb://ihsuprodpnres007dednamespace.servicebus.windows.net/";
    private static final String EVENT_HUBS_COMPATIBLE_PATH = "iothub-ehub-spidey-iot-17309678-034bba6000";
    private static final String IOT_HUB_SAS_KEY = "cbWwyhwFbF6ljBjPxDL4uI/YwbsFKMkK7sbU0ujagxw=";
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
                String eventHubCompatibleConnectionString = String.format(EH_COMPATIBLE_CONNECTION_STRING_FORMAT,
                        EVENT_HUBS_COMPATIBLE_ENDPOINT, EVENT_HUBS_COMPATIBLE_PATH, IOT_HUB_SAS_KEY_NAME,
                        IOT_HUB_SAS_KEY);
               // myFile = new FileWriter("DropMsgLogs-" + Math.random() + ".txt");
                EventHubClientBuilder eventHubClientBuilder = null;
                try {
                    eventHubClientBuilder = new EventHubClientBuilder()
                            .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                            .connectionString(eventHubCompatibleConnectionString);
                } catch (Exception e) {
                    System.err.println("Error occured " + e.getMessage());
                }
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
                System.out.println("Failed to receive");
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
    private static void logIndexToFile(String str) throws IOException {
        myFile.append(str);
    }

    private static void setupProxy(EventHubClientBuilder eventHubClientBuilder) {
        int proxyPort = 80; // replace with right proxy port
        String proxyHost = "proxy-privzen.jfwtc.ge.com";
        Proxy proxyAddress = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        String userName = null;
        String password = null;
        ProxyOptions proxyOptions = new ProxyOptions(ProxyAuthenticationType.BASIC, proxyAddress,
                userName, password);

        eventHubClientBuilder.proxyOptions(proxyOptions);
        eventHubClientBuilder.transportType(AmqpTransportType.AMQP_WEB_SOCKETS);
    }
}
