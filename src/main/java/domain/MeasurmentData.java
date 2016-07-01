package domain;

import java.util.List;

/**
 * Created by 212303924 on 2016.02.07..
 */
public class MeasurmentData {

    List<DifferenceDataPoint> differenceDataPointList;
    List<SensorData> sensorDataList;
    long centerOfGravityX;
    long centerOfGravityY;

    public List<DifferenceDataPoint> getDifferenceDataPointList() {
        return differenceDataPointList;
    }

    public List<SensorData> getSensorDataList() {
        return sensorDataList;
    }

    public long getCenterOfGravityX() {
        return centerOfGravityX;
    }

    public long getCenterOfGravityY() {
        return centerOfGravityY;
    }

    public MeasurmentData( List<DifferenceDataPoint> differenceDataPointList, List<SensorData> sensorDataList, long centerOfGravityX, long centerOfGravityY ) {
        this.differenceDataPointList = differenceDataPointList;
        this.sensorDataList = sensorDataList;
        this.centerOfGravityX = centerOfGravityX;
        this.centerOfGravityY = centerOfGravityY;
    }
}
