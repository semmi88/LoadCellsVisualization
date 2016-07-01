package transducer;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 212303924 on 2016.06.12..
 */
public class AedEmulatorTest {

    AedEmulator underTest = new AedEmulator();

    @Test
    public void testReadAnswer() throws Exception {
        byte[] bytes = underTest.readAnswer( null, 10 );
        String string = new String( bytes, StandardCharsets.UTF_8 );
        System.out.println( string );
    }
}