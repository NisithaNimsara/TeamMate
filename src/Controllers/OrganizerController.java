package Controllers;

import Models.*;
import ValidatorHelp.ConsoleInput;
import ValidatorHelp.FileProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// This controller handles all actions that an Organizer can do:
// Load/import participants from CSV
// Form teams
// View and export formed teams
public class OrganizerController {
    private final ParticipantRepository repository;  // This will access to participants data
    private final ConsoleInput input;
    private final TeamBuilder teamBuilder;

    //store the teams formed
    private List<Team> currentTeams = new ArrayList<>();

    //Constructor
    public OrganizerController(ParticipantRepository repository, ConsoleInput input,  TeamBuilder teamBuilder) {
        this.repository = repository;
        this.input = input;
        this.teamBuilder = teamBuilder;

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
            System.out.printf("\nImported %d participants, ignored %d.%n", result[0], result[1]);
        } catch (FileProcessingException e){
            System.out.println("Import Failed: "+e.getMessage());
        }

        //calling teamFormation()
        teamFormation();
    }

    // load data from given CSV file.
    public void importExternalCSV(){
        String fileName = input.readLine("Enter external CSV file name to import: ");

        try{
            int[] result = repository.importFromCsvFile(fileName);
            System.out.printf("\nImported %d participants, ignored %d.%n", result[0], result[1]);
        }  catch (FileProcessingException e){
            System.out.println("Import Failed: "+e.getMessage());
        }

        //calling teamFormation()
        teamFormation();
    }

    //this function will form team
    private void teamFormation(){
        System.out.println("\n--Team Formation--");
        //ask team size
        int teamSize = input.readIntInRange("Enter Team Size: ",1,100);
        List<Participant> participants = new ArrayList<>(repository.getAll());

        if(participants.isEmpty()){
            System.out.println("No participants available for team formation.");
            return;
        }

        //assign to a tread using TeamFormationThread class
        TeamFormationThread thread = new TeamFormationThread(teamBuilder, participants, teamSize);
        thread.start();

        try{
            // wait until background thread finishing forming teams
            thread.join();

            if (thread.getError() != null){
                System.out.println("Error forming teams: "+thread.getError().getMessage());
                return;
            }

            currentTeams = thread.getResult();
            if(currentTeams == null){
                System.out.println("No teams were formed. ");
                return;
            }

            System.out.println("Teams formed successfully. Total teams: "+currentTeams.size());

        } catch (InterruptedException e){
            System.out.println("Team formation thread interrupted: " + e.getMessage());
        }
    }

}
