package interactivecommandexecutor;


import java.util.Scanner;

/**
 * Created by endre on 8/8/2015.
 */
public class Controller {

    View view;
    Model model;
    public static final String MENU =
            "Select an option, enter a number \n\r" +
            "0 - exit \n\r" +
            "1 - connect to a port \n\r" +
            "2 - send command \n\r" +
            "3 - read answer \n\r" +
            "4 - measure & store all sensor values";
    public static final String WELCOME = "!!! Pressure Measuring Cells Application Prototype !!! - Started \n\r";
    public static final String UNKNOWN_COMMAND = "Unknown command. Try again \n\r";

    public void setModel(Model model) {
        this.model = model;
    }

    public void startReceivingInput(){
        String input;
        view.display(WELCOME);
        view.display(MENU);
        do {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            switch (input){
                case "0":
                    model.closeStreams();
                    break;
                case "1":
                    model.createNewPortConnection();
                    break;
                case "2":
                    model.sendCommand();
                    break;
                case "3":
                    model.readAnswer();
                    break;
                case "4":
                    model.measureValues();
                    break;
                default:
                    view.display(UNKNOWN_COMMAND);
                    view.display(MENU);
            }
        } while (!input.equals("0"));
    }

    public void setView(View view) {
        this.view = view;
    }
}
