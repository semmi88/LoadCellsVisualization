package repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.SensorData;

/**
 * Created by 212303924 on 2016.06.12..
 */
public class LoadFromFile {

    private final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss:SSS" );

    public List<SensorData> loadDataFromFile( String fileName ) {
        List<SensorData> returnSensorDataList = new ArrayList<>();
        Path path = Paths.get( fileName );
        try {
            try ( BufferedReader reader = Files.newBufferedReader( path, StandardCharsets.UTF_8 ) ) {
                for ( String line = reader.readLine(); line != null; line = reader.readLine() ) {

                    if ( line.isEmpty() ) {
                        continue;
                    }

                    String[] splitted = line.split( "\t" );

                    if ( splitted.length != 5 ) {
                        String errorMessage = "ERROR: cannot read file, line does not contain 5 elements";
                        System.out.println( errorMessage );
                        throw new RuntimeException( errorMessage );
                    }

                    String time = splitted[0];
                    Date date = formatter.parse( time );
                    long timeStamp = date.getTime();

                    String firstSensorDataValue = splitted[1];
                    String secondSensorDataValue = splitted[2];
                    String thirdSensorDataValue = splitted[3];
                    String fourthSensorDataValue = splitted[4];

                    SensorData firstSensorData = new SensorData.Builder().timeStamp( timeStamp ).sensorId( "S01" ).dataValue( firstSensorDataValue.trim().getBytes() ).build();
                    SensorData secondSensorData = new SensorData.Builder().timeStamp( timeStamp ).sensorId( "S02" ).dataValue( secondSensorDataValue.trim().getBytes() ).build();
                    SensorData thirdSensorData = new SensorData.Builder().timeStamp( timeStamp ).sensorId( "S03" ).dataValue( thirdSensorDataValue.trim().getBytes() ).build();
                    SensorData fourthSensorData = new SensorData.Builder().timeStamp( timeStamp ).sensorId( "S04" ).dataValue( fourthSensorDataValue.trim().getBytes() ).build();

                    returnSensorDataList.add( firstSensorData );
                    returnSensorDataList.add( secondSensorData );
                    returnSensorDataList.add( thirdSensorData );
                    returnSensorDataList.add( fourthSensorData );
                }
            }
        } catch ( IOException | ParseException e ) {
            e.printStackTrace();
        }
        return returnSensorDataList;
    }
}
