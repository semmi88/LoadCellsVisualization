package transducer;

import domain.SensorData;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by endre on 9/7/2015.
 */
public interface HighLevelCommunicator {

    List<SensorData> measureContinouslySlow( OutputStream outputStream, InputStream inputStream, List<String> sensorIds, long measurementDuration );
    List<SensorData> measureOneFast( OutputStream outputStream, InputStream inputStream, List<String> sensorIds );
    void selectAllSensors(OutputStream outputStream);
    void resetSensorsToZero(OutputStream outputStream);
    void measureFifthy(OutputStream outputStream, InputStream inputStream, List<String> sensorIds);
}
