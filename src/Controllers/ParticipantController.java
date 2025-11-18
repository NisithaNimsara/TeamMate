package Controllers;


import Models.PersonalityClassifier;
import ValidatorHelp.ConsoleInput;

import java.awt.*;

// This will handles everything related to "Participants".
public class ParticipantController {

    private final PersonalityClassifier classifier;

    //Constructor
    public ParticipantController(PersonalityClassifier classifier) {
        this.classifier = classifier;
    }

    // participaant menu
    public void participantMenu(){
        ConsoleInput input = null;
        boolean running = true;

        while(running){
            System.out.println("\n--- Participant Menu ---");
            System.out.println("1. New registration");
            System.out.println("2. View my details");
            System.out.println("0. Back");

            int choice = input.readInt("Select your choice: ");

            switch (choice) {
                case 1:
                    // handle registration
                    break;
                case 2:
                    // vies details
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
