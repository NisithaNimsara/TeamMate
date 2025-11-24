import Controllers.AppController;
import Controllers.OrganizerController;
import Controllers.ParticipantController;
import Models.ParticipantRepository;
import Models.PersonalityClassifier;
import Models.TeamBuilder;
import ValidatorHelp.ConsoleInput;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TeamMateApp {

    private static final String SYSTEM_PARTICIPANT_FILE = "participants_sample.csv";

    private static final Logger logger = Logger.getLogger(TeamMateApp.class.getName());

    public static void main(String[] args) {

        logger.info("TeamMate application Starting");
        logger.info("System participant file configured as: " + SYSTEM_PARTICIPANT_FILE);

        try{
            Scanner scanner = new Scanner(System.in);
            ConsoleInput input = new ConsoleInput(scanner);

            PersonalityClassifier classifier = new PersonalityClassifier();
            ParticipantRepository repository = new ParticipantRepository(SYSTEM_PARTICIPANT_FILE);
            TeamBuilder teamBuilder = new TeamBuilder();

            //controllers
            ParticipantController participantController = new ParticipantController(classifier, repository, input);
            OrganizerController organizerController = new OrganizerController(repository, input, teamBuilder);
            AppController appController = new AppController(input, participantController, organizerController);

            //------------------------------------------------------------------------------------------------------------
            //Main app run
            appController.run();

        } catch (Exception e) {
            logger.log(Level.SEVERE,"Unexpected error in TeamMateApp main method.", e);
        }

        logger.info("TeamMate application Terminated");
    }
}
