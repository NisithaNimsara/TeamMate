import Controllers.AppController;
import Controllers.ParticipantController;
import Models.ParticipantRepository;
import Models.PersonalityClassifier;
import ValidatorHelp.ConsoleInput;

import java.util.Scanner;

public class TeamMateApp {

    private static final String SYSTEM_PARTICIPANT_FILE = "participants_sample.csv";

    public static void main(String[] args) {
        Scanner scanner =  new Scanner(System.in);
        ConsoleInput input = new ConsoleInput(scanner);
        PersonalityClassifier classifier = new PersonalityClassifier();
        ParticipantRepository repository = new ParticipantRepository(SYSTEM_PARTICIPANT_FILE);

        //controllers
        ParticipantController participantController = new ParticipantController(classifier, repository, input);
        AppController appController = new AppController(input, participantController);

        appController.run();
    }

}
