package Controllers;

import ValidatorHelp.ConsoleInput;

public class AppController {
    private final ConsoleInput input;
    private final ParticipantController pController;
    private final OrganizerController oController;

    public AppController(ConsoleInput input, ParticipantController p, OrganizerController o) {
        this.input = input;
        this.pController = p;
        this.oController = o;
    }

    public void run() {
        while (true) {
            System.out.println("\n--- TeamMate Application ---");
            System.out.println("1. Participant Mode");
            System.out.println("2. Organizer Mode");
            System.out.println("0. Exit");

            int choice = input.readInt("Select your choice: ", 0, 2);
            if (choice == 0)
                break;

            if (choice == 1)
                //Participant Menu
                pController.showMenu();
            else
                //Organizer menu
                oController.showMenu();
        }
        System.out.println("Exiting application...");
    }
}