import Controllers.AppController;
import ValidatorHelp.ConsoleInput;

import java.util.Scanner;

public class TeamMateApp {

    public static void main(String[] args) {
        Scanner scanner =  new Scanner(System.in);
        ConsoleInput input = new ConsoleInput(scanner);
        AppController appController = new AppController(input);

        appController.run();
    }

}
