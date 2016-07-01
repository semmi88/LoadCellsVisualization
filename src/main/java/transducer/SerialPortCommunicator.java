package transducer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by endre on 5/31/2015.
 */
public interface SerialPortCommunicator {

    void sendCommand(OutputStream outputStream, byte[] data);

    byte[] readAnswer(InputStream inputStream, int numberOfBytes);
}
