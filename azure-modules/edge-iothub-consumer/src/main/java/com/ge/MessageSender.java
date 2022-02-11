package com.ge;

import java.util.Random;

import com.google.gson.Gson;
import com.microsoft.azure.iothub.DeviceClient;
import com.microsoft.azure.iothub.Message;
import com.microsoft.azure.iothub.IotHubStatusCode;
import com.microsoft.azure.iothub.IotHubEventCallback;
public class MessageSender implements Runnable {
    public volatile boolean stopThread = false;
    private static String deviceId = "spidey";
    private static DeviceClient client;
    
    public void run()  {
      try {
        double avgWindSpeed = 10; // m/s
        Random rand = new Random();
        
        while (!stopThread) {
          double currentWindSpeed = avgWindSpeed + rand.nextDouble() * 4 - 2;
          TelemetryDataPoint telemetryDataPoint = new TelemetryDataPoint();
          telemetryDataPoint.deviceId = deviceId;
          telemetryDataPoint.windSpeed = currentWindSpeed;
          
          String msgStr = telemetryDataPoint.serialize();
          Message msg = new Message(msgStr);
          System.out.println("Sending: " + msgStr);
          
          Object lockobj = new Object();
          EventCallback callback = new EventCallback();
          client.sendEventAsync(msg, callback, lockobj);
          
          synchronized (lockobj) {
            lockobj.wait();
          }
          Thread.sleep(1000);
        }
      } catch (InterruptedException e) {
        System.out.println("Finished.");
      }
    }
    private static class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }

    public class TelemetryDataPoint {
        public String deviceId;
        public double windSpeed;

        public String serialize() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
  }
  