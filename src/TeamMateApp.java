import Controllers.*;
import Models.*;
import ValidatorHelp.ConsoleInput;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamMateApp {
    private static final Logger logger = Logger.getLogger(TeamMateApp.class.getName());

    public static void main(String[] args) {
        logger.info("Starting TeamMate application");
        try {
            //Initialize Helpers
            ConsoleInput input = new ConsoleInput(new Scanner(System.in));

            //Initialize Models
            ParticipantRepository repo = new ParticipantRepository("participants_sample.csv");
            PersonalityClassifier classifier = new PersonalityClassifier();
            TeamBuilder builder = new TeamBuilder();

            //Initialize Controllers
            ParticipantController pc = new ParticipantController(repo, classifier, input);
            OrganizerController oc = new OrganizerController(repo, builder, input);
            AppController app = new AppController(input, pc, oc);

            // Main app Run
            app.run();
        }catch (Exception e){
            logger.log(Level.SEVERE,"Unexpected error in TeamMateApp main method.",e.getMessage());
        }
        logger.info("TeamMate application Terminated");
    }
}