package domain;

import gnu.io.SerialPort;


/**
 * Created by endre on 7/21/2015.
 */
public class PortConfiguration {

    private final String name;

    private final int portOpenTimeoutMilliSec;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    private PortConfiguration(Builder builder) {
        this.name = builder.portName;
        this.portOpenTimeoutMilliSec = builder.portOpenTimeoutMilliSec;
        this.baudRate = builder.baudRate;
        this.dataBits = builder.dataBits;
        this.stopBits = builder.stopBits;
        this.parity = builder.parity;
    }

    public static class Builder {

        private String portName;

        private int portOpenTimeoutMilliSec;

        private int baudRate;
        private int dataBits;
        private int stopBits;
        private int parity;

        public Builder defaultValuesWithName(String portName){
            this.portName = portName;
            this.portOpenTimeoutMilliSec = 2000;
            this.baudRate = 115200;
            this.dataBits = SerialPort.DATABITS_8;
            this.stopBits = SerialPort.STOPBITS_1;
            this.parity = SerialPort.PARITY_NONE;
            return this;
        }

        public Builder name(String portName) {
            this.portName = portName;
            return this;
        }

        public Builder baudRate(int baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        public Builder dataBits(int dataBits) {
            this.dataBits = dataBits;
            return this;
        }

        public Builder stopBits(int stopBits) {
            this.stopBits = stopBits;
            return this;
        }

        public Builder portOpenTimeoutMilliSec(int portOpenTimeoutMilliSec) {
            this.portOpenTimeoutMilliSec = portOpenTimeoutMilliSec;
            return this;
        }

        public Builder parity(int parity) {
            this.parity = parity;
            return this;
        }

        public PortConfiguration build() {
            return new PortConfiguration(this);
        }
    }

    public String getName() {
        return name;
    }

    public int getPortOpenTimeoutMilliSec() {
        return portOpenTimeoutMilliSec;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    @Override
    public String toString() {
        return "PortConfiguration{" +
                "portName='" + name + '\'' +
                ", baudRate=" + baudRate +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                ", portOpenTimeoutMilliSec=" + portOpenTimeoutMilliSec +
                '}';
    }
}
