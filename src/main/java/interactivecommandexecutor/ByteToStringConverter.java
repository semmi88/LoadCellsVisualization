package interactivecommandexecutor;

import java.nio.charset.StandardCharsets;

/**
 * Created by 212303924 on 2015.11.22..
 */
public class ByteToStringConverter {

    public static String byte2String( byte[] data ) {
        String returnValue = new String( data, StandardCharsets.UTF_8 );
        return returnValue.trim();
    }

    private static boolean isEndLineCharacter( byte b ) {
        return b == 10 || b == 13;
    }
}
