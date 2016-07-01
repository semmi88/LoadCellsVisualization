package interactivecommandexecutor;


import java.util.List;

import domain.DifferenceDataPoint;
import domain.MeasurmentData;
import domain.SensorData;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

/**
 * Created by 212303924 on 2016.02.07..
 */
public class FxControllerV2 {

    MeasurmentData lastMeasuredData;

    Model model;

    @FXML
    TextField measureContinouslyForTextField;
    @FXML
    TextField displayValuesPerSecondTextField;
    @FXML
    ScatterChart<Number, Number> centerOfGravityChart;
    @FXML
    LineChart<Number, Number> onwardDiffChart;
    @FXML
    LineChart<Number, Number> sidewaysDiffChart;
    @FXML
    TextField saveAsTextField;
    @FXML
    TextField loadFileTextField;
    @FXML
    TextField referencePointsTextField;
    @FXML
    TextField scaleTimeAxisTextField;
    @FXML
    TextField scaleCellValueTextField;
    @FXML
    TextField intervalStartTextField;
    @FXML
    TextField intervalStopTextField;

    @FXML
    public void initialize() {
        model = Model.getInstance();

        reset();
        drawReferencePoints( Integer.parseInt( referencePointsTextField.getText() ) );

        intervalStopTextField.setText( Integer.toString( Integer.parseInt( measureContinouslyForTextField.getText() ) * 1000 ) );
    }

    private void drawReferencePoints( int referencePoint ) {
        XYChart.Series<Number, Number> centerOfGravityInitSeries = new XYChart.Series();
        centerOfGravityInitSeries.setName( "Boundary values" );
        centerOfGravityInitSeries.getData().add( new XYChart.Data( 0L, referencePoint ) );
        centerOfGravityInitSeries.getData().add( new XYChart.Data( 0L, -referencePoint ) );
        centerOfGravityInitSeries.getData().add( new XYChart.Data( referencePoint, 0L ) );
        centerOfGravityInitSeries.getData().add( new XYChart.Data( -referencePoint, 0L ) );
        centerOfGravityChart.getData().add( centerOfGravityInitSeries );
        centerOfGravityChart.setTitle( "Center of gravity" );

        XYChart.Series onwardInitSeries = new XYChart.Series();
        onwardInitSeries.setName( "Boundary values" );
        onwardInitSeries.getData().add( new XYChart.Data( 0, -referencePoint ) );
        onwardInitSeries.getData().add( new XYChart.Data( 0, referencePoint ) );
        onwardDiffChart.getData().add( onwardInitSeries );
        onwardDiffChart.setTitle( "Onward difference" );

        sidewaysDiffChart.setAxisSortingPolicy( LineChart.SortingPolicy.Y_AXIS );
        XYChart.Series sidewaysInitSeries = new XYChart.Series();
        sidewaysInitSeries.setName( "Boundary values" );
        sidewaysInitSeries.getData().add( new XYChart.Data( -referencePoint, 0 ) );
        sidewaysInitSeries.getData().add( new XYChart.Data( referencePoint, 0 ) );
        sidewaysDiffChart.getData().add( sidewaysInitSeries );
        sidewaysDiffChart.setTitle( "Sideways difference" );
    }

    @FXML
    public void reset() {
        model.measureFifthy();
        model.measureFifthy();
        model.resetToZero();
    }

    @FXML
    public void measureContinouslyFor() {
        String seconds = measureContinouslyForTextField.getText();

        long startTime = System.currentTimeMillis();
        MeasurmentData measurmentData = model.measureCenterOfGravity( Integer.parseInt( seconds ) );
        System.out.print( "Total time: " );
        System.out.println( System.currentTimeMillis() - startTime );

        drawCenterOfGravity( measurmentData );
        drawDifferencesOnCharts( measurmentData );

        lastMeasuredData = measurmentData;
    }

    private void drawCenterOfGravity( MeasurmentData measurmentData ) {

        XYChart.Series centerOfGravitySeries = new XYChart.Series();
        centerOfGravitySeries.setName( "Center of gravity" );

        List<SensorData> sensorDataList = measurmentData.getSensorDataList();
        long startTime = sensorDataList.get( 0 ).getTimeStamp();

        double scaleTime = Double.parseDouble( scaleTimeAxisTextField.getText() );
        double scaleCellValues = Double.parseDouble( scaleCellValueTextField.getText() );

        double intervalStart = Double.parseDouble( intervalStartTextField.getText() );
        double intervalStop = Double.parseDouble( intervalStopTextField.getText() );

        long sums[] = {0, 0, 0, 0};
        long count = 0;

        for ( int i = 0; i < sensorDataList.size(); i = i + 4 ) {
            count++;
            long currentTime = sensorDataList.get( i ).getTimeStamp();
            long timeDiff = currentTime - startTime;
            double scaledTimeDiff = timeDiff * scaleTime;

            if ( scaledTimeDiff >= intervalStart && scaledTimeDiff <= intervalStop ) {
                for ( int j = 0; j <= 3; j++ ) {
                    byte[] valueInBytes = sensorDataList.get( j + i ).getDataValue();
                    int value = Integer.parseInt( ByteToStringConverter.byte2String( valueInBytes ) );
                    sums[j] += value * scaleCellValues;
                }
            }
        }

        long centerOfGravityX = (-sums[0] - sums[3] + sums[2] + sums[1]) / count;
        long centerOfGravityY = (-sums[0] - sums[2] + sums[1] + sums[3]) / count;

        centerOfGravitySeries.getData().add( new XYChart.Data<>( centerOfGravityX, centerOfGravityY ) );
        centerOfGravityChart.getData().add( centerOfGravitySeries );
    }

    private void drawDifferencesOnCharts( MeasurmentData measurmentData ) {
        List<DifferenceDataPoint> dataPointList = measurmentData.getDifferenceDataPointList();
        XYChart.Series onwardDiffSeries = new XYChart.Series();
        XYChart.Series sidewaysDiffSeries = new XYChart.Series();

        double scaleTime = Double.parseDouble( scaleTimeAxisTextField.getText() );
        double scaleCellValues = Double.parseDouble( scaleCellValueTextField.getText() );

        double intervalStart = Double.parseDouble( intervalStartTextField.getText() );
        double intervalStop = Double.parseDouble( intervalStopTextField.getText() );

        int skippedValues = 60 / getValuesPerSecond();
        long startTimeStamp = dataPointList.get( 0 ).getTimeStamp();
        for ( int i = 0; i < dataPointList.size(); i = i + skippedValues ) {
            DifferenceDataPoint currentDataPoint = dataPointList.get( i );
            Long time = currentDataPoint.getTimeStamp() - startTimeStamp;
            double scaledTimeDiff = time * scaleTime;
            if ( scaledTimeDiff >= intervalStart && scaledTimeDiff <= intervalStop ) {
                onwardDiffSeries.getData().add( new XYChart.Data<>( scaledTimeDiff, -currentDataPoint.getOnward() * scaleCellValues ) );
                sidewaysDiffSeries.getData().add( new XYChart.Data<>( currentDataPoint.getSideways() * scaleCellValues, scaledTimeDiff ) );
            }
        }

        onwardDiffSeries.setName( "Measured differences" );
        sidewaysDiffSeries.setName( "Measured differences" );
        onwardDiffChart.getData().add( onwardDiffSeries );
        sidewaysDiffChart.getData().add( sidewaysDiffSeries );
    }

    @FXML
    public void redrawGraphs() {
        if ( lastMeasuredData != null ) {
            clearGraphsStartingFromSeriesWithIndex( 0 );
            drawReferencePoints( Integer.parseInt( referencePointsTextField.getText() ) );
            drawCenterOfGravity( lastMeasuredData );
            drawDifferencesOnCharts( lastMeasuredData );
        }
    }

    @FXML
    public void saveAs() {
        String fileName = saveAsTextField.getText();

        if ( lastMeasuredData != null ) {
            model.saveDataToFile( lastMeasuredData.getSensorDataList(), fileName );
            System.out.println( "Data saved to file " + fileName );
        } else {
            System.out.println( "No data saved, there were no measured values. Measure first!" );
        }

        saveAsTextField.setText( fileName + "_temp" );
    }

    @FXML
    public void loadFile() {
        String fileName = loadFileTextField.getText();

        if ( lastMeasuredData != null ) {
            List<SensorData> sensorDatas = model.loadDataFromFile( fileName );
            System.out.println( "Data loaded from " + fileName );

            lastMeasuredData = model.convertSenorDataToMeasurementData( sensorDatas );
            redrawGraphs();
        }
    }

    @FXML
    public void clearGraph() {
        clearGraphsStartingFromSeriesWithIndex( 0 );
        clearGraphsStartingFromSeriesWithIndex( 1 );
        drawReferencePoints( Integer.parseInt( referencePointsTextField.getText() ) );
    }

    private void clearGraphsStartingFromSeriesWithIndex( int index ) {
        if ( !centerOfGravityChart.getData().isEmpty() ) {
            centerOfGravityChart.getData().remove( index, centerOfGravityChart.getData().size() );
        }

        if ( !onwardDiffChart.getData().isEmpty() ) {
            onwardDiffChart.getData().remove( index, onwardDiffChart.getData().size() );
        }

        if ( !sidewaysDiffChart.getData().isEmpty() ) {
            sidewaysDiffChart.getData().remove( index, sidewaysDiffChart.getData().size() );
        }
    }

    private int getValuesPerSecond() {
        //return valuesPerSecondChoiceBox.getValue();
        int valuesPerSecond = Integer.parseInt( displayValuesPerSecondTextField.getText() );
        if ( valuesPerSecond > 60 ) {
            valuesPerSecond = 60 % valuesPerSecond;
            displayValuesPerSecondTextField.setText( "60" );
        }
        return valuesPerSecond;
    }
}
