package portfactory;

import domain.PortConfiguration;
import gnu.io.SerialPort;

/**
 * Created by endre on 8/8/2015.
 */
public interface PortConnectionFactory {

    public SerialPort createPortConnection(PortConfiguration portConfiguration);
}
