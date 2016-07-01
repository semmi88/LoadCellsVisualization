package interactivecommandexecutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import portfactory.FakePortConnectionFactory;
import portfactory.SerialPortConnectionFactory;
import transducer.Aed;
import transducer.AedEmulator;
import transducer.Measurer;
import transducer.SerialPortCommunicator;

import java.io.IOException;

/**
 * Created by endre on 8/8/2015.
 */
public class RunPMC extends Application {

    // Sensor positions
    //   |
    // 3  1
    // 2  4
    //
    public static void main(String[] args) throws IOException {
        Model model = Model.getInstance();
        View view = new View();
        model.setView(view);

        //SerialPortCommunicator serialPortCommunicator = new AedEmulator();
        SerialPortCommunicator serialPortCommunicator = new Aed();
        model.setSerialPortCommunicator(serialPortCommunicator);

        model.setPortConnectionFactory( new SerialPortConnectionFactory() );
        //model.setPortConnectionFactory( new FakePortConnectionFactory() );
        model.setHighLevelCommunicator(new Measurer(serialPortCommunicator));

        //Controller controller = new Controller();
        //controller.setModel(model);
        //controller.setView(view);

        //controller.startReceivingInput();
        model.createNewPortConnection();
        launch( args );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui_v2.fxml"));
        primaryStage.setTitle("Measurement Software");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }
}
