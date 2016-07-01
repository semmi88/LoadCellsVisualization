package domain.exception;

/**
 * Created by endre on 7/22/2015.
 */
public class PortConnectionException extends RuntimeException {

    public PortConnectionException(String message, Throwable cause){
        super(message, cause);
    }

    public PortConnectionException(String message){
        super(message);
    }
}
