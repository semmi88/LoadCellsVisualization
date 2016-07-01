package presentation.impl;

import communicator.SensorCommunicator;
import communicator.impl.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by endre on 7/15/2015.
 */
@Configuration
@ComponentScan (value = "{presentation.presentation.impl}")
public class DIPresentationConfiguration {

    @Bean
    public SensorCommunicator getSensorCommunicator(){
        return new Controller();
    }
}
