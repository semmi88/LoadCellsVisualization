package transducer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by endre on 8/10/2015.
 */
public class Aed implements SerialPortCommunicator {
    @Override
    public void sendCommand(OutputStream outputStream, byte[] data) {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] readAnswer(InputStream inputStream, int numberOfBytes) {
        byte[] readBytesBuffer = new byte[numberOfBytes];
        try {
            while (((inputStream.read(readBytesBuffer)) > -1)) {
                return readBytesBuffer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
