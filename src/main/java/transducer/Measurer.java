package transducer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import domain.SensorData;

/**
 * Created by endre on 9/7/2015.
 */
public class Measurer implements HighLevelCommunicator {

    SerialPortCommunicator serialPortCommunicator;

    public Measurer( SerialPortCommunicator serialPortCommunicator ) {
        this.serialPortCommunicator = serialPortCommunicator;
    }

    @Override
    public void selectAllSensors( OutputStream outputStream ) {
        send( outputStream, AedConstants.clearBuffer );
        send( outputStream, AedConstants.highestOutputRate );
        send( outputStream, AedConstants.selectAll );
        send( outputStream, AedConstants.ASCIIOutputFormat );
    }

    @Override
    public void resetSensorsToZero( OutputStream outputStream ) {
        send( outputStream, AedConstants.selectAll );
        sendAndWait( outputStream, AedConstants.tar, 10 );
    }

    @Override
    public void measureFifthy( OutputStream outputStream, InputStream inputStream, List<String> sensorIds ) {
        int numberOfBytesToRead = 10;
        selectAllSensors( outputStream );

        for ( int i = 0; i < 50; i++ ) {
            send( outputStream, AedConstants.selectAll );
            sendAndWait( outputStream, AedConstants.measure, 3 );
            for ( String sensorId : sensorIds ) {
                sendAndWait( outputStream, (sensorId + ";").getBytes(), 3 );
                byte[] measuredValue = serialPortCommunicator.readAnswer( inputStream, numberOfBytesToRead );
            }
        }
    }

    @Override
    public List<SensorData> measureOneFast( OutputStream outputStream, InputStream inputStream, List<String> sensorIds ) {
        int numberOfBytesToRead = 10;
        List<SensorData> sensorDataList = new ArrayList<>( sensorIds.size() );
        long startTime = System.currentTimeMillis();
        System.out.println( "START " + System.currentTimeMillis() );
        selectAllSensors( outputStream );
        System.out.println( "Init sensors, finished " + System.currentTimeMillis() );
        send( outputStream, AedConstants.selectAll );
        sendAndWait( outputStream, AedConstants.measure, 10 );
        System.out.println( "Measure one value, finished " + System.currentTimeMillis() );
        long actualTime = System.currentTimeMillis();

        for ( String sensorId : sensorIds ) {
            sendAndWait( outputStream, (sensorId + ";").getBytes(), 5 );
            System.out.println( "Get " + sensorId + ", finished " + System.currentTimeMillis() );
            byte[] measuredValue = serialPortCommunicator.readAnswer( inputStream, numberOfBytesToRead );
            System.out.println( "Read from AED " + sensorId + ", finished " + System.currentTimeMillis() );
            SensorData sensorData = new SensorData.Builder()
                    .timeStamp( System.currentTimeMillis() )
                    .dataValue( measuredValue )
                    .sensorId( sensorId )
                    .build();
            sensorDataList.add( sensorData );
            System.out.println( "Sensordata " + sensorId + " constructed, finished " + System.currentTimeMillis() );
        }
        System.out.println( "DONE " + System.currentTimeMillis() );
        return sensorDataList;
    }

    @Override
    public List<SensorData> measureContinouslySlow( OutputStream outputStream, InputStream inputStream, List<String> sensorIds, long measurementDurationInSeconds ) {
//        int numberOfBytesToRead = 50;
//        List<SensorData> sensorDataList = new LinkedList<>();
//
//        long startTime = System.currentTimeMillis();
//
//        selectAllSensors( outputStream );
//
//        do {
//            send( outputStream, "S01;".getBytes() );
//            sendAndWait( outputStream, AedConstants.measureFive, 50 );
//            long actualTime = System.currentTimeMillis();
//
//            //for ( String sensorId : sensorIds ) {
//                sendAndWait( outputStream, "S01;".getBytes(), 5 );
//            //}
//            byte[] measuredValue = serialPortCommunicator.readAnswer( inputStream, numberOfBytesToRead );
//
//            for ( int i = 0; i < 5; i++ ) {
//                byte[] valueForOnSensor = new byte[10];
//                for ( int j = 0; j < 10; j++ ) {
//                    valueForOnSensor[j] = measuredValue[i * 10 + j];
//                }
//                SensorData sensorData = new SensorData.Builder()
//                        .timeStamp( System.currentTimeMillis() )
//                        .dataValue( valueForOnSensor )
//                        .sensorId( sensorIds.get( 0 ) )
//                        .build();
//                sensorDataList.add( sensorData );
//            }
//
//        } while ( (System.currentTimeMillis() - startTime) < measurementDurationInSeconds * 1000 );
//
//        return sensorDataList;

        int numberOfBytesToRead = 40;
        long startTime = System.currentTimeMillis();

        List<SensorData> sensorDataList = new LinkedList<>();

        selectAllSensors( outputStream );


        int MEASURE_GROUP_SIZE = 30;
        int MEASURE_GROUP_PERIOD_TIME_MILLIS = ((Double) (1000.0 / 60.0 * MEASURE_GROUP_SIZE)).intValue(); // 200 ~ 12 * 1000/60
        int MEASURE_MULTIPLIER = 1000 / MEASURE_GROUP_PERIOD_TIME_MILLIS; // 5

        for ( int i = 0; i < MEASURE_MULTIPLIER * measurementDurationInSeconds; i++ ) {
            long startPeriod = System.currentTimeMillis();

            for ( int j = 0; j < MEASURE_GROUP_SIZE; j++ ) {
                measureSensorData( outputStream, inputStream, sensorIds, numberOfBytesToRead, sensorDataList );
            }

            long elapsedTime = System.currentTimeMillis() - startPeriod;
            long waitTime = MEASURE_GROUP_PERIOD_TIME_MILLIS - elapsedTime;
            System.out.println( "Wait time is: " + waitTime );
            if ( waitTime > 0 ) {
                try {
                    Thread.sleep( waitTime );
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }

//        long measuremntDurationInMillis = measurementDurationInSeconds * 1000;
//        do {
//            measureSensorData( outputStream, inputStream, sensorIds, numberOfBytesToRead, sensorDataList );
//
//        } while ( (System.currentTimeMillis() - startTime) < measuremntDurationInMillis );

        return sensorDataList;
    }

    private void measureSensorData( OutputStream outputStream, InputStream inputStream, List<String> sensorIds, int numberOfBytesToRead, List<SensorData> sensorDataList ) {
        send( outputStream, AedConstants.selectAll );
        sendAndWait( outputStream, AedConstants.measure, 3 );

        for ( String sensorId : sensorIds ) {
            sendAndWait( outputStream, (sensorId + ";").getBytes(), 3 );
        }

        long actualTime = System.currentTimeMillis();
        byte[] measuredValue = serialPortCommunicator.readAnswer( inputStream, numberOfBytesToRead );

        for ( int i = 0; i < sensorIds.size(); i++ ) {
            byte[] valueForOnSensor = new byte[10];
            for ( int j = 0; j < 10; j++ ) {
                valueForOnSensor[j] = measuredValue[i * 10 + j];
            }
            String valueInString = new String( valueForOnSensor, StandardCharsets.UTF_8 ).trim();
            SensorData sensorData = new SensorData.Builder()
                    .timeStamp( actualTime )
                    .dataValue( valueForOnSensor )
                    .sensorId( sensorIds.get( i ) )
                    .build();
            sensorDataList.add( sensorData );
        }
    }

    private void sendAndWait( OutputStream outputStream, byte[] bytes, int millisecToWait ) {
        serialPortCommunicator.sendCommand( outputStream, bytes );
        wait( millisecToWait );
    }

    private void send( OutputStream outputStream, byte[] bytes ) {
        serialPortCommunicator.sendCommand( outputStream, bytes );
    }

    private String dataToString( byte[] measuredValue ) {
        String data = null;
        int length = 0;
        for ( byte b : measuredValue ) {
            if ( b != 0 ) {
                length++;
            }
        }

        byte[] newMeasuredValues = new byte[length];
        for ( int i = 0; i < length; i++ ) {
            newMeasuredValues[i] = measuredValue[i];
        }
        data = new String( newMeasuredValues, StandardCharsets.UTF_8 );

        return data;
    }

    private void wait( int millis ) {
        try {
            Thread.sleep( millis );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}
