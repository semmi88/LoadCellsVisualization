package transducer;

/**
 * Created by endre on 9/7/2015.
 */
public final class AedConstants {

    private AedConstants(){}

    public static byte[] selectAll = "S98;".getBytes();
    public static byte[] measure = "MSV?;".getBytes();
    public static byte[] measureFive = "MSV?5;".getBytes();
    public static byte[] ASCIIOutputFormat = "COF3;".getBytes();
    public static byte[] clearBuffer = ";".getBytes();
    public static byte[] highestOutputRate = "ICR0;".getBytes();
    public static byte[] tar = "TAR;".getBytes();

}
