package transducer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by endre on 7/9/2015.
 */
public class AedEmulator implements SerialPortCommunicator {

    Random randomGenerator = new Random( System.currentTimeMillis() );

    @Override
    public void sendCommand( OutputStream outputStream, byte[] data ) {

    }

    @Override
    public byte[] readAnswer( InputStream inputStream, int numberOfBytes ) {

        int numbersToGenerate = numberOfBytes / 10;
        byte[] answerArray = new byte[numberOfBytes];

        for ( int i = 0; i < numbersToGenerate; i++ ) {
            int randomNumber = randomGenerator.nextInt( 20000 );
            randomNumber = randomNumber - 10000;
            String randomNumberString = new Integer( randomNumber ).toString();
            byte[] numberInByteArray = randomNumberString.getBytes();

            for (int j = 0; j < numberInByteArray.length; j++) {
                answerArray[i*10+j] = numberInByteArray[j];
            }
        }

        return answerArray;
    }
}
