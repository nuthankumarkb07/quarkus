package com.ge;

import com.microsoft.azure.eventhubs.*;
import java.util.function.*;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class ConsumerVerticle extends AbstractVerticle {
  // private String connStr =
  // "Endpoint=sb://ihsuprodpnres007dednamespace.servicebus.windows.net/;
  // SharedAccessKeyName=iothubowner;
  // SharedAccessKey=D0V1VD4nX/c1hDvYu1If9O/JYivPrXeA5LFdFlC/YQY=;
  // EntityPath=iothub-ehub-spidey-iot-17309678-034bba6000;";

  // private static final String EH_COMPATIBLE_CONNECTION_STRING_FORMAT = "Endpoint=%s/;EntityPath=%s;"
  //     + "SharedAccessKeyName=%s;SharedAccessKey=%s";
  // private static final String EVENT_HUBS_COMPATIBLE_ENDPOINT = "sb://ihsuprodpnres007dednamespace.servicebus.windows.net/";
  // private static final String EVENT_HUBS_COMPATIBLE_PATH = "iothub-ehub-spidey-iot-17309678-034bba6000";
  // private static final String IOT_HUB_SAS_KEY = "D0V1VD4nX/c1hDvYu1If9O/JYivPrXeA5LFdFlC/YQY=";
  // private static final String IOT_HUB_SAS_KEY_NAME = "iothubowner";


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

  private static final Logger logger = LoggerFactory.getLogger(ConsumerVerticle.class);

  private EventHubClient receiveMessages(final String partitionId) throws EventHubException {
    EventHubClient client = null;
    try {
      client = EventHubClient.createFromConnectionStringSync(String.format(EH_COMPATIBLE_CONNECTION_STRING_FORMAT,
          EVENT_HUBS_COMPATIBLE_ENDPOINT, EVENT_HUBS_COMPATIBLE_PATH, IOT_HUB_SAS_KEY_NAME,
          IOT_HUB_SAS_KEY), new IotExecutorService());
    } catch (Exception e) {
      System.out.println("Failed to create client: " + e.getMessage());
      System.exit(1);
    }
    try {
      client.createReceiver(
          EventHubClient.DEFAULT_CONSUMER_GROUP_NAME,
          partitionId,
          new IotEventPosition()).thenAccept(new Consumer<PartitionReceiver>() {
            public void accept(PartitionReceiver receiver) {
              System.out.println("** Created receiver on partition " + partitionId);
              try {
                while (true) {
                  Iterable<EventData> receivedEvents = receiver.receive(100).get();
                  int batchSize = 0;
                  if (receivedEvents != null) {
                    for (EventData receivedEvent : receivedEvents) {
                      System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s",
                          receivedEvent.getSystemProperties().getOffset(),
                          receivedEvent.getSystemProperties().getSequenceNumber(),
                          receivedEvent.getSystemProperties().getEnqueuedTime()));
                      System.out.println(String.format("| Device ID: %s",
                          receivedEvent.getProperties().get("iothub-connection-device-id")));
                      // System.out.println(String.format("| Message Payload: %s", new
                      // String(receivedEvent.getBody(),
                      // Charset.defaultCharset())));
                      batchSize++;
                    }
                  }
                  System.out.println(String.format("Partition: %s, ReceivedBatch Size: %s", partitionId, batchSize));
                }
              } catch (Exception e) {
                System.out.println("Failed to receive messages: " + e.getMessage());
              }
            }
          });
    } catch (Exception e) {
      System.out.println("Failed to create receiver: " + e.getMessage());
    }
    return client;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.setPeriodic(1000, x -> {
      try {
        receiveMessages("spidey");
      } catch (EventHubException e) {
        logger.info("Failed to receive");
        e.printStackTrace();
      }
    });
  }
}
