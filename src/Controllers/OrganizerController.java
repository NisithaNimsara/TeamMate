package Controllers;

import Models.ParticipantRepository;
import ValidatorHelp.ConsoleInput;
import ValidatorHelp.FileProcessingException;

// This controller handles all actions that an Organizer can do:
// Load/import participants from CSV
// Form teams
// View and export formed teams
public class OrganizerController {
    private final ParticipantRepository repository;  // This will access to participants data
    private final ConsoleInput input;

    //Constructor
    public OrganizerController(ParticipantRepository repository, ConsoleInput input) {
        this.repository = repository;
        this.input = input;

    }
    // Organizer menu.
    public void organizerMenu(ConsoleInput input){
        boolean running = true;
        while(running){
            System.out.println("\n--- Organizer Menu ---");
            System.out.println("1. Import participants from system CSV.");
            System.out.println("2. Import participants from external CSV.");
            System.out.println("0. Back");
            int choice = input.readInt("Select your choice: ");
            switch (choice) {
                case 1:
                    importSystemCSV();
                    break;
                case 2:
                    importExternalCSV();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // Load date from the default system CSV file.
    public void importSystemCSV(){
        String fileName = "participants_sample.csv"; //fixed file

        try{
            int[] result = repository.importFromCsvFile(fileName);
            System.out.printf("Imported %d participants, ignored %d.%n", result[0], result[1]);
        } catch (FileProcessingException e){
            System.out.println("Import Failed: "+e.getMessage());
        }
    }

    // load data from given CSV file.
    public void importExternalCSV(){
        String fileName = input.readLine("Enter external CSV file name to import: ");

        try{
            int[] result = repository.importFromCsvFile(fileName);
            System.out.printf("Imported %d participants, ignored %d.%n", result[0], result[1]);
        }  catch (FileProcessingException e){
            System.out.println("Import Failed: "+e.getMessage());
        }

    }

}
