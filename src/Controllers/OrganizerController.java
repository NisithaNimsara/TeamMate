package Controllers;

import Models.*;
import ValidatorHelp.ConsoleInput;
import ValidatorHelp.FileProcessingException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
            System.out.println("1. Import participants from system CSV. and form Teams");
            System.out.println("2. Import participants from external CSV. and form Teams");
            System.out.println("3. view formed Teams");
            System.out.println("0. Back");
            int choice = input.readInt("Select your choice: ");
            switch (choice) {
                case 1:
                    importSystemCSV();
                    break;
                case 2:
                    importExternalCSV();
                    break;
                case 3:
                    viewTeams();
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
            return;
        }

        //calling teamFormation()
        teamFormation();
    }

    //this method will form team
    private void teamFormation(){
        System.out.println("\n-- Team Formation --");
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


    //this method will
    private void viewTeams(){
        if(currentTeams == null || currentTeams.isEmpty()){
            System.out.println("No teams available. Form teams first (Using Organizer's Option 1 or 2).");
            return;
        }

        System.out.println("\n--- Current Teams ---");
        for (Team team : currentTeams){
            System.out.println(team);
            System.out.println();
        }

        // Ask whether to export teams or not.
        int choice = input.readIntInRange("Export teams to CSV? (1=Yes, 0=No): ",0,1);
        switch (choice){
            case 1:
                exportTeams();
                break;
        }
    }

    private void exportTeams(){
        while (true){
            String fileName = input.readLine("Enter export file name (e.g., teams.csv): ");
            if (fileName != null  && fileName.matches("^[A-Za-z0-9+_.-]+\\.csv$")){

                try(FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)){
                    bw.write("TeamId,ParticipantId,Name,Email,Game,Skill,Role,PersonalityScore,PersonalityType\n");
                    for (Team team : currentTeams){
                        for(Participant p : team.getMembers()){
                            bw.write(team.toCSVRow(p));
                            bw.newLine();
                        }
                    }
                    System.out.println("\nTeams exported Successfully to "+fileName);
                    break;

                } catch (IOException e){
                    System.out.println("Export failed: " + e.getMessage());
                    break;
                }

            } else {
                System.out.println("please enter a valid file name extension .\nEg: something.csv");
            }
        }
    }
}
