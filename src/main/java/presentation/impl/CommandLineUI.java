package presentation.impl;

import communicator.SensorCommunicator;
import communicator.impl.Controller;
import domain.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by endre on 7/15/2015.
 */
@Component
public class CommandLineUI {

    {
        sensorCommunicator = new Controller();
    }

    @Autowired
    private static SensorCommunicator sensorCommunicator;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println();
        System.out.println("Starting Pressure Measuring Cells Application - prototype!");
        System.out.println();

        Thread.sleep(1000);

        System.out.println("Fake data:");
        List<SensorData> sensorDataList = sensorCommunicator.realAllSensorDataNow();
        for (SensorData sensorData : sensorDataList) {
            System.out.println(sensorData);
        }

        Thread.sleep(1000);

        System.out.println();
        System.out.println("Press enter to exit...");
        System.in.read();
    }
}
