package portfactory;

import domain.PortConfiguration;
import domain.exception.PortConnectionException;
import gnu.io.*;

/**
 * Created by endre on 7/21/2015.
 */
public class SerialPortConnectionFactory implements PortConnectionFactory {

    public SerialPort createPortConnection(PortConfiguration portConfiguration) {
        SerialPort returnPort;
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portConfiguration.getName());
            checkPortIsNotInUse(portIdentifier);

            CommPort commPort = portIdentifier.open(this.getClass().getName(), portConfiguration.getPortOpenTimeoutMilliSec());
            checkPortIsSerialPort(commPort);
            SerialPort serialPort = (SerialPort) commPort;

            serialPort.setSerialPortParams(portConfiguration.getBaudRate(),
                    portConfiguration.getDataBits(),
                    portConfiguration.getStopBits(),
                    portConfiguration.getParity());

            returnPort = serialPort;

        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException e) {
            throw new PortConnectionException("Error: ", e);
        }
        return returnPort;
    }

    private void checkPortIsSerialPort(CommPort commPort) {
        if (!(commPort instanceof SerialPort)) {
            throw new PortConnectionException("Error: Only serial ports are handled by this example.");
        }
    }

    private void checkPortIsNotInUse(CommPortIdentifier portIdentifier) {
        if (portIdentifier.isCurrentlyOwned()) {
            throw new PortConnectionException("Error: Port is currently in use");
        }
    }
}
