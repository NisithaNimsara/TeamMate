package Controllers;

import ValidatorHelp.ConsoleInput;

public class AppController {
    private final ConsoleInput input;

    public AppController(ConsoleInput input) {
        this.input = input;
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
                    // Option 1 → Participant Menu
                    break;
                case 2:
                    // Option 2 → Organizer Menu
                    break;
                case 0:
                    // Option 0 → Exit the app
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
