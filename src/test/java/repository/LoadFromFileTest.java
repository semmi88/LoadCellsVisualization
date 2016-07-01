package repository;

import java.util.List;

import org.junit.Test;

import domain.SensorData;

/**
 * Created by 212303924 on 2016.06.12..
 */
public class LoadFromFileTest {

    LoadFromFile underTest = new LoadFromFile();
    SaveToFile saveToFile = new SaveToFile();

    @Test
    public void testName() throws Exception {
        List<SensorData> sensorDatas = underTest.loadDataFromFile( "korte.txt" );
        saveToFile.saveDataToFile( "alma.txt", sensorDatas );
    }
}
