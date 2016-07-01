package communicator.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import transducer.SerialPortCommunicator;
import transducer.AedEmulator;

/**
 * Created by endre on 7/10/2015.
 */
@Configuration
@ComponentScan (value = {"communicator.presentation.impl"})
public class DIControllerConfiguration {

    @Bean
    public SerialPortCommunicator getSerialPortCommunicator(){
        return new AedEmulator();
    }
}
