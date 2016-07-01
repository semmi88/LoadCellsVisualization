package communicator.impl;

import communicator.SensorCommunicator;
import domain.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import transducer.SerialPortCommunicator;
import transducer.AedEmulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by endre on 7/9/2015.
 */
@Component
public class Controller implements SensorCommunicator {

    {
        serialPortCommunicator = new AedEmulator();
    }
    @Autowired
    SerialPortCommunicator serialPortCommunicator;

    @Override
    public List<SensorData> realAllSensorDataNow() {
        SensorData sensorData = new SensorData.Builder().
                sensorId("S01").
                dataValue(new byte[]{123,123,120}).
                dataFormat("ASCII").
                timeStamp(System.currentTimeMillis()).
                build();

        List<SensorData> dataList = new ArrayList<>();
        dataList.add(sensorData);
        return dataList;
    }
}
