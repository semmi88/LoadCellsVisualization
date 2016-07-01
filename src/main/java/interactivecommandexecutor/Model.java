package interactivecommandexecutor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import domain.DifferenceDataPoint;
import domain.MeasurmentData;
import domain.PortConfiguration;
import domain.SensorData;
import gnu.io.SerialPort;
import portfactory.PortConnectionFactory;
import repository.LoadFromFile;
import repository.SaveToFile;
import transducer.HighLevelCommunicator;
import transducer.SerialPortCommunicator;

/**
 * Created by endre on 8/8/2015.
 */
public class Model {

    SerialPort serialPort;

    View view;

    private static Model instance;

    String propertyFileName = "commands.properties";
    Properties properties;

    PortConnectionFactory portConnectionFactory;
    SerialPortCommunicator serialPortCommunicator;
    HighLevelCommunicator highLevelCommunicator;
    SaveToFile saveToFile = new SaveToFile();
    LoadFromFile loadFromFile = new LoadFromFile();

    private InputStream inputStream;
    private OutputStream outputStream;
    private final List<String> sensorIdList = Arrays.asList( "S01", "S02", "S03", "S04" );

    private Model() {
        refreshValuesFromPropertyFile();
    }

    public static Model getInstance() {
        if ( instance == null ) {
            instance = new Model();
        }
        return instance;
    }

    public void createNewPortConnection() {
        refreshValuesFromPropertyFile();
        PortConfiguration portConfiguration = new PortConfiguration.Builder()
                .name( properties.getProperty( "portName" ) )
                .baudRate( Integer.parseInt( properties.getProperty( "baudRate" ) ) )
                .dataBits( Integer.parseInt( properties.getProperty( "dataBits" ) ) )
                .stopBits( Integer.parseInt( properties.getProperty( "stopBits" ) ) )
                .parity( Integer.parseInt( properties.getProperty( "parity" ) ) )
                .portOpenTimeoutMilliSec( Integer.parseInt( properties.getProperty( "portOpenTimeoutMilliSec" ) ) )
                .build();

        view.display( portConfiguration.toString() );
        serialPort = portConnectionFactory.createPortConnection( portConfiguration );
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        view.display( "Succeeded" );
    }

    public void sendCommand() {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return;
        }
        refreshValuesFromPropertyFile();
        String commandToSend = properties.getProperty( "commandToSend" );
        view.display( "Sending command: " + commandToSend );
        serialPortCommunicator.sendCommand( outputStream, commandToSend.getBytes() );
    }

    public void sendCommand( String command ) {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return;
        }
        view.display( "Sending command: " + command );
        serialPortCommunicator.sendCommand( outputStream, command.getBytes() );
    }

    public void readAnswer() {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return;
        }
        refreshValuesFromPropertyFile();
        int bytesToReadAsAnswer = Integer.parseInt( properties.getProperty( "bytesToReadAsAnswer" ) );
        byte[] bytes = serialPortCommunicator.readAnswer( inputStream, bytesToReadAsAnswer );
        String data;

        data = new String( bytes, StandardCharsets.UTF_8 );
        view.display( "Answer read: " + data );
    }

    public String readAnswer( int bytesToRead ) {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return "Error";
        }
        byte[] bytes = serialPortCommunicator.readAnswer( inputStream, bytesToRead );
        String data;

        data = new String( bytes, StandardCharsets.UTF_8 );
        view.display( "Answer read: " + data );
        return data;
    }

    public List<SensorData> measureValues() {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return null;
        }
        refreshValuesFromPropertyFile();
        List<String> sensorIdList = Arrays.asList( "S01", "S02", "S03", "S04" );
        view.display( "Will measure values for sensors with ids: " + sensorIdList.toString() );
        long measurementDurationInSeconds = Long.parseLong( properties.getProperty( "measurementDurationInSeconds" ) );
        view.display( "Measurement will last " + measurementDurationInSeconds + " seconds..." );
        List<SensorData> sensorDataList = highLevelCommunicator.measureContinouslySlow( outputStream, inputStream, sensorIdList, measurementDurationInSeconds );
        String filename = properties.getProperty( "fileNameToSaveMeasuredValues" );
        view.display( "Measurement completed. Saving data to file " + filename );
        saveToFile.saveDataToFile( filename, sensorDataList );
        view.display( "Saving to file completed." );
        return sensorDataList;
    }

    public MeasurmentData measureCenterOfGravity( int seconds ) {
        if ( serialPort == null ) {
            view.display( "Connect to a serial port first..." );
            return null;
        }

        refreshValuesFromPropertyFile();
        view.display( "Will measure values for sensors with ids: " + sensorIdList.toString() );
        view.display( "Measurement will last " + seconds + " seconds..." );
        List<SensorData> sensorDataList = highLevelCommunicator.measureContinouslySlow( outputStream, inputStream, sensorIdList, seconds );
        String filename = properties.getProperty( "fileNameToSaveMeasuredValues" );
        view.display( "Measurement completed. Saving data to file " + filename );

        //saveToFile.saveDataToFile( filename, sensorDataList );
        //view.display( "Saving to file completed." );

        return convertSenorDataToMeasurementData( sensorDataList );
    }

    public MeasurmentData convertSenorDataToMeasurementData( List<SensorData> sensorDataList ) {
        List<DifferenceDataPoint> dataPointList = new ArrayList<>();
        long sum1 = 0;
        long sum2 = 0;
        long sum3 = 0;
        long sum4 = 0;
        int exceptions = 0;
        int goodResultCount = 0;
        for ( int i = 0; i < sensorDataList.size(); i = i + 4 ) {
            boolean wasThereAnError = false;

            SensorData sensorData1 = sensorDataList.get( i );
            long temp1 = 0;
            try {
                temp1 = Integer.parseInt( ByteToStringConverter.byte2String( sensorData1.getDataValue() ) );
            } catch ( NumberFormatException e ) {
                wasThereAnError = true;
            }

            SensorData sensorData2 = sensorDataList.get( i + 1 );
            long temp2 = 0;
            try {
                temp2 = Integer.parseInt( ByteToStringConverter.byte2String( sensorData2.getDataValue() ) );
            } catch ( NumberFormatException e ) {
                wasThereAnError = true;
            }

            SensorData sensorData3 = sensorDataList.get( i + 2 );
            long temp3 = 0;
            try {
                temp3 = Integer.parseInt( ByteToStringConverter.byte2String( sensorData3.getDataValue() ) );
            } catch ( NumberFormatException e ) {
                wasThereAnError = true;
            }

            SensorData sensorData4 = sensorDataList.get( i + 3 );
            long temp4 = 0;
            try {
                temp4 = Integer.parseInt( ByteToStringConverter.byte2String( sensorData4.getDataValue() ) );
            } catch ( NumberFormatException e ) {
                wasThereAnError = true;
            }

            if ( !wasThereAnError ) {
                goodResultCount++;
                sum1 += temp1;
                sum2 += temp2;
                sum3 += temp3;
                sum4 += temp4;
                DifferenceDataPoint dataPoint = new DifferenceDataPoint(
                        temp3 + temp1 - temp2 - temp4,
                        temp3 + temp2 - temp1 - temp4,
                        sensorData1.getTimeStamp() );
                dataPointList.add( dataPoint );
            } else {
                exceptions++;
            }
        }
        System.out.println( "Good Results: " + goodResultCount );
        System.out.println( "Exceptions:   " + exceptions );
        long centerOfGravityX = (-sum1 - sum4 + sum3 + sum2) / goodResultCount;
        long centerOfGravityY = (-sum1 - sum3 + sum2 + sum4) / goodResultCount;
        return new MeasurmentData( dataPointList, sensorDataList, centerOfGravityX, centerOfGravityY );
    }

    public List<SensorData> measureOne() {
        List<String> sensorIdList = Arrays.asList( "S01", "S02", "S03", "S04" );
        return highLevelCommunicator.measureOneFast( outputStream, inputStream, sensorIdList );
    }

    public void selectAllSensors() {
        highLevelCommunicator.selectAllSensors( outputStream );
    }

    private void refreshValuesFromPropertyFile() {
        try ( InputStream inputStream = new BufferedInputStream( new FileInputStream( propertyFileName ) ) ) {
            properties = new Properties();
            properties.load( inputStream );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void closeStreams() {
        try {
            if ( inputStream != null ) {
                inputStream.close();
            }
            if ( outputStream != null ) {
                outputStream.close();
            }
        } catch ( IOException e ) {
        }
    }

    private String dataToString( byte[] measuredValue ) {
//        String data = null;
//
//        int length = 0;
//        for ( byte b : measuredValue ) {
//            if (b!=48){
//                length++;
//            }
//        }
//
//        byte[] newMeasuredValues = new byte[length];
//        for ( int i = 0; i < length; i++ ) {
//            newMeasuredValues[i] = measuredValue[i];
//        }
//        try {
//            data = new String(newMeasuredValues, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return data;
        return new String( measuredValue );
    }

    public void setSerialPortCommunicator( SerialPortCommunicator serialPortCommunicator ) {
        this.serialPortCommunicator = serialPortCommunicator;
    }

    public void setPortConnectionFactory( PortConnectionFactory portConnectionFactory ) {
        this.portConnectionFactory = portConnectionFactory;
    }

    public void setView( View view ) {
        this.view = view;
    }

    public void setHighLevelCommunicator( HighLevelCommunicator highLevelCommunicator ) {
        this.highLevelCommunicator = highLevelCommunicator;
    }

    public void resetToZero() {
        highLevelCommunicator.resetSensorsToZero( outputStream );
    }

    public void measureFifthy() {
        highLevelCommunicator.measureFifthy( outputStream, inputStream, sensorIdList );
    }

    public void saveDataToFile( List<SensorData> lastMeasuredDataSet, String fileName ) {
        saveToFile.saveDataToFile( fileName, lastMeasuredDataSet );
    }

    public List<SensorData> loadDataFromFile( String fileName ) {
        return loadFromFile.loadDataFromFile( fileName );
    }
}
