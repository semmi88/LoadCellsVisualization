package communicator;

import domain.SensorData;

import java.util.List;

/**
 * Created by endre on 7/9/2015.
 */
public interface SensorCommunicator {

    List<SensorData> realAllSensorDataNow();
}
