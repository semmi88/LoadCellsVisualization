package repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import domain.SensorData;

/**
 * Created by endre on 9/7/2015.
 */
public class SaveToFile {

    public static final String TAB = "\t";
    public static final String WRONG_SENSOR_DATA = "N/A";
    SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss:SSS" );

    public void saveDataToFile( String fileName, List<SensorData> sensorDataList ) {
        Path path = Paths.get( fileName );
        try {
            try ( BufferedWriter writer = Files.newBufferedWriter( path, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND ) ) {
                Iterator<SensorData> iterator = sensorDataList.iterator();
                while ( iterator.hasNext() ) {
                    SensorData firstSensorData = iterator.next();

                    String firstSensorId = firstSensorData.getSensorId();
                    if ( !firstSensorId.equals( "S01" ) ) {
                        System.out.println( "ERROR: sensor id mismatch" );
                        throw new RuntimeException( "Sensor id mismatch for " + firstSensorData.toString() );
                    }

                    long timeStamp = firstSensorData.getTimeStamp();
                    Date date = new Date( timeStamp );
                    writer.write( formatter.format( date ) );

                    writer.write( TAB );
                    writer.write( new String( firstSensorData.getDataValue(), StandardCharsets.UTF_8 ).trim() );

                    writeNextSensorDataToFile( writer, iterator, timeStamp, "S02" );
                    writeNextSensorDataToFile( writer, iterator, timeStamp, "S03" );
                    writeNextSensorDataToFile( writer, iterator, timeStamp, "S04" );
                    writer.newLine();
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void writeNextSensorDataToFile( BufferedWriter writer, Iterator<SensorData> iterator, long timeStamp, String sensorId ) throws IOException {
        if ( iterator.hasNext() ) {
            SensorData sensorData = iterator.next();
            if ( !sensorData.getSensorId().equals( sensorId ) ) {
                System.out.println( "ERROR: sensor id mismatch" );
                throw new RuntimeException( "Sensor id mismatch for " + sensorData.toString() );
            }
            if ( timeStamp != sensorData.getTimeStamp() ) {
                System.out.printf( "ERROR: timestamp is different for sensor " + sensorId );
                writer.write( TAB );
                writer.write( WRONG_SENSOR_DATA );
            }
            writer.write( TAB );
            writer.write( new String( sensorData.getDataValue(), StandardCharsets.UTF_8 ).trim() );
        } else {
            System.out.printf( "ERROR: data missing for sensor " + sensorId );
            writer.write( TAB );
            writer.write( WRONG_SENSOR_DATA );
        }
    }
}
