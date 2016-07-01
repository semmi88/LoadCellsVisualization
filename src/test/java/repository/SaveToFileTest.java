package repository;

import domain.SensorData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by endre on 9/7/2015.
 */
public class SaveToFileTest {

    SaveToFile underTest = new SaveToFile();
    private final SensorData firstSensorData = new SensorData.Builder()
            .sensorId("S01")
            .timeStamp(System.currentTimeMillis())
            .dataValue(new Integer(1000).toString().getBytes())
            .build();
    private final SensorData secondSensorData = new SensorData.Builder()
            .sensorId("S02")
            .timeStamp(System.currentTimeMillis())
            .dataValue(new Integer(2000).toString().getBytes())
            .build();
    private final SensorData thirdSensorData = new SensorData.Builder()
            .sensorId("S03")
            .timeStamp(System.currentTimeMillis())
            .dataValue(new Integer(3000).toString().getBytes())
            .build();
    private final SensorData fourthSensorData = new SensorData.Builder()
            .sensorId("S04")
            .timeStamp(System.currentTimeMillis())
            .dataValue(new Integer(4000).toString().getBytes())
            .build();


    @Test
    public void testStoreToFile() throws Exception {
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorDataList.add( firstSensorData );
        sensorDataList.add( secondSensorData );
        sensorDataList.add( thirdSensorData );
        sensorDataList.add( fourthSensorData );
        underTest.saveDataToFile("alma.txt", sensorDataList);
        underTest.saveDataToFile("alma.txt", sensorDataList);
    }

    @Test (expected = RuntimeException.class)
    public void testName() throws Exception {
        List<SensorData> sensorDataList = new ArrayList<>();
        sensorDataList.add( firstSensorData );
        sensorDataList.add( fourthSensorData );
        underTest.saveDataToFile("alma.txt", sensorDataList);
    }
}
