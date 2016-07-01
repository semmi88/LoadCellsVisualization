package interactivecommandexecutor;

import java.util.List;

import domain.SensorData;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

public class FxController {

    @FXML
    TextArea answerTextArea;
    @FXML
    TextArea commandTextArea;
    @FXML
    javafx.scene.control.TextField commandTextField;
    @FXML
    javafx.scene.control.TextField measureTextField;
    @FXML
    javafx.scene.control.TextField answerTextField;
    @FXML
    ScatterChart<Number, Number> chart;
    @FXML
    javafx.scene.control.Label sensorLabel1;
    @FXML
    javafx.scene.control.Label sensorLabel2;
    @FXML
    javafx.scene.control.Label sensorLabel3;
    @FXML
    javafx.scene.control.Label sensorLabel4;
    @FXML
    javafx.scene.control.Label perpendicularShiftLabel;
    @FXML
    javafx.scene.control.Label parallelShiftLabel;

    Model model;

    @FXML
    public void initialize() {
        model = Model.getInstance();

        XYChart.Series series = new XYChart.Series();
        series.setName( "Boundary values" );
        series.getData().add( new XYChart.Data<>( 0, 10000 ) );
        series.getData().add( new XYChart.Data<>( 0, -10000 ) );
        series.getData().add( new XYChart.Data<>( 10000, 0 ) );
        series.getData().add( new XYChart.Data<>( -10000, 0 ) );
        chart.getData().add( series );
    }

    @FXML
    private void sendCommandButton() {
        String command = commandTextField.getText();
        model.sendCommand( command );
        commandTextArea.appendText( command + "\n" );
    }

    @FXML
    private void readBytesButton() {
        String bytesToRead = answerTextField.getText();
        String resultData = model.readAnswer( Integer.parseInt( bytesToRead ) );
        answerTextArea.appendText( resultData + "\n" );
    }

    @FXML
    private void measureOne() {
        List<SensorData> sensorData = model.measureOne();

        for ( SensorData data : sensorData ) {
            switch ( data.getSensorId() ) {
                case "S01":
                    sensorLabel1.setText( new String( data.getDataValue() ) );
                    break;
                case "S02":
                    sensorLabel2.setText( new String( data.getDataValue() ) );
                    break;
                case "S03":
                    sensorLabel3.setText( new String( data.getDataValue() ) );
                    break;
                case "S04":
                    sensorLabel4.setText( new String( data.getDataValue() ) );
                    break;
            }
        }
    }

//    public void measureContinously() {
//        String seconds = measureTextField.getText();
//
//        long startTime = System.currentTimeMillis();
//        Long[] shifts = model.measureCenterOfGravity( Integer.parseInt( seconds ) );
//        System.out.print( "Total time: " );
//        System.out.println( System.currentTimeMillis() - startTime );
//
//        Long leftRightDiff = shifts[0];
//        Long forwardBackwardDiff = shifts[1];
//
//        parallelShiftLabel.setText( leftRightDiff.toString() );
//        perpendicularShiftLabel.setText( forwardBackwardDiff.toString() );
//
//        XYChart.Series series = new XYChart.Series();
//        series.setName( "Sensor data sum" );
//        //series.getData().add( new XYChart.Data<>( 0 , forwardBackwardDiff ) );
//        //series.getData().add( new XYChart.Data<>( leftRightDiff, 0 ) );
//        series.getData().add( new XYChart.Data<>( leftRightDiff, forwardBackwardDiff ) );
//        chart.getData().add( series );
//
//    }


    public void clearGraph() {
        if ( !chart.getData().isEmpty() ) {
            chart.getData().remove( 1, chart.getData().size() );
        }
    }

    public void setModel( Model model ) {
        this.model = model;
    }

    @FXML
    public void resetToZero() {
        model.resetToZero();
    }

    public void measureFifthy() {
        model.measureFifthy();
    }
}
