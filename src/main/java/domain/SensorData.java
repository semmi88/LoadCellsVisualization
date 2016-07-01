package domain;

/**
 * Created by endre on 5/30/2015.
 */
public class SensorData {

    private final String sensorId;
    private final byte[] dataValue;
    private final String dataFormat;
    private final long timeStamp;

    private SensorData(Builder builder) {
        this.sensorId = builder.sensorId;
        this.dataValue = builder.dataValue;
        this.dataFormat = builder.dataFormat;
        this.timeStamp = builder.timeStamp;
    }
    public static class Builder{
        private String sensorId;
        private byte[] dataValue;
        private String dataFormat;
        private long timeStamp;

        public Builder sensorId(String _sensorId){
            sensorId = _sensorId;
            return this;
        }

        public Builder dataValue(byte[] _dataValue){
            dataValue = _dataValue;
            return this;
        }

        public Builder dataFormat(String _format){
            dataFormat = _format;
            return this;
        }

        public Builder timeStamp(long _timeStamp){
            timeStamp = _timeStamp;
            return this;
        }
        public SensorData build(){
            return new SensorData(this);
        }

    }

    public String getDataFormat() {
        return dataFormat;
    }

    public String getSensorId() {
        return sensorId;
    }

    public byte[] getDataValue() {
        return dataValue;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return  "{sensorId='" + sensorId + '\'' +
                ", dataValue=" + new String(dataValue) +
                ", dataFormat='" + dataFormat + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
