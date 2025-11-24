package Controllers;

import ValidatorHelp.ConsoleInput;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController {
    private final ConsoleInput input;
    private final ParticipantController participantController;
    private final OrganizerController organizerController;

    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    public AppController(ConsoleInput input, ParticipantController participantController, OrganizerController organizerController) {
        this.input = input;
        this.participantController = participantController;
        this.organizerController = organizerController;
    }

    // This method starts the whole application.
    public void run() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- TeamMate Application ---");
            System.out.println("1. Participant");
            System.out.println("2. Organizer");
            System.out.println("0. Exit");
            int choice = input.readInt("Select your choice: ");

            switch (choice) {
                case 1:
                    // Participant Menu
                    participantController.participantMenu(input);
                    break;
                case 2:
                    organizerController.organizerMenu(input);
                    break;
                case 0:
                    // Exit the app
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    // Any other number is invalid
                    logger.warning("Invalid menu choice entered: (" + choice + ") Please try again.");
            }
        }
    }
}
