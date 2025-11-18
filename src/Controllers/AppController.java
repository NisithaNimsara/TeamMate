package Controllers;

import Models.ParticipantRepository;
import ValidatorHelp.ConsoleInput;

public class AppController {
    private final ConsoleInput input;
    private final ParticipantController participantController;

    public AppController(ConsoleInput input, ParticipantController participantController) {
        this.input = input;
        this.participantController = participantController;
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
                    // Option 2 → Organizer Menu
                    break;
                case 0:
                    // Exit the app
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    // Any other number is invalid
                    System.out.println("Invalid choice.");
            }
        }
    }
}
