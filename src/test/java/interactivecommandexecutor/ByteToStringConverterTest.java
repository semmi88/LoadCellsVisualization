package interactivecommandexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Created by 212303924 on 2015.11.22..
 */
public class ByteToStringConverterTest {

    ByteToStringConverter underTest = new ByteToStringConverter();

    List<String> testData= Arrays.asList(
            "10",
            "-10",
            "01",
            "-001" ,
            "10\r\n",
            "-10\r\n",
            "-00200\n",
            " 90",
            "  -0033\r\n");

    List<Integer> expectedConvertedValues = Arrays.asList(
            10,
            -10,
            1,
            -1,
            10,
            -10,
            -200,
            90,
            -33);

    @Test
    public void testName() throws Exception {
        for ( int i = 0; i < testData.size(); i++ ) {
            String convertedValue = underTest.byte2String( testData.get( i ).getBytes());
            Integer parsedInt = Integer.parseInt( convertedValue );
            System.out.println(parsedInt);
            assertEquals( expectedConvertedValues.get( i ), parsedInt );
        }
    }


}
