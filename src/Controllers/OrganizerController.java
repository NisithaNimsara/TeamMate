package Controllers;

import Models.*;
import ValidatorHelp.ConsoleInput;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizerController {
    private final ParticipantRepository repo;
    private final TeamBuilder builder;
    private final ConsoleInput input;
    private List<Team> currentTeams;
    private List<Participant> leftovers;
    private static final Logger logger = Logger.getLogger(OrganizerController.class.getName());

    public OrganizerController(ParticipantRepository repo, TeamBuilder builder, ConsoleInput input) {
        this.repo = repo;
        this.builder = builder;
        this.input = input;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Organizer Menu ---");
            System.out.println("1. Import External CSV");
            System.out.println("2. Form Teams");
            System.out.println("3. View and Export Teams");
            System.out.println("4. View Remaining Participants");
            System.out.println("0. Back");

            int choice = input.readInt("Select your choice: ", 0, 4);
            if (choice == 0) break;

            switch (choice) {
                case 1: importCSV(); break;
                case 2: formTeams(); break;
                case 3: viewAndExport(); break;
                case 4: viewRemaining(); break;
            }
        }
    }
                                                              // 3.1
    private void importCSV() {
        String file = input.readLine("Enter Filename(without extension): ");     // 3.2
        try {
            int[] res = repo.importExternalFile(file+".csv");                    // 3.3
            System.out.printf("\nImported %d participants, ignored %d.", res[0], res[1]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Import CSV failed!", e.getMessage());
        }
    }
                                                           // 4.1
    private void formTeams() {
        System.out.println("\n-- Team Formation --");
        int size = input.readInt("Enter Team Size (2-10): ", 2, 10);     // 4.2

        TeamFormationThread t = new TeamFormationThread(builder, repo.getAll(), size);    // 4.3 and 4.4
        t.start();                                                                        // 4.5
        try {
            t.join();                                                                     // 4.6
            if (t.getError() != null) throw t.getError();                                 // 4.7

            TeamFormationResult result = t.getResult();                                   // 4.8

            this.currentTeams = result.getTeams();                                        // 4.9
            this.leftovers = result.getLeftovers();                                       // 4.10

            if (currentTeams.isEmpty()) {
                System.out.println("Could not form any valid teams.");
            } else {
                System.out.println("Success! Formed " + currentTeams.size() + " teams.");
                System.out.println("Remaining participants: " + leftovers.size());
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Formation failed!", e.getMessage());
        }
    }
                                              //5.1
    private void viewAndExport() {
        if (currentTeams == null) {
            System.out.println("No teams formed yet.");
            return;
        }
        System.out.println();
        currentTeams.forEach(System.out::println);                    // 5.2

        if (input.readInt("Export to CSV? (1=Yes, 0=No): ", 0, 1) == 1) {         // 5.3
            String file = input.readLine("Enter export file name(without extension): ");    // 5.4

            try (PrintWriter writer = new PrintWriter(new FileWriter(file+".csv"))) {      // 5.5
                writer.println("TeamID,ID,Name,Email,Game,Skill,Role,Score,Type");                 // 5.6
                for (Team t : currentTeams) {
                    for (Participant p : t.getMembers()) {                                         // 5.7
                        writer.println(t.toCSVRow(p));                                             // 5.8 and 5.9
                    }
                }
                System.out.println("\nTeams exported Successfully to "+file+".csv");
            } catch (IOException e) {
                logger.log(Level.WARNING, "Export CSV failed!", e.getMessage());
            }
        }
    }
                                                                     // 6.1
    private void viewRemaining(){
        if (currentTeams == null){
            System.out.println("No teams formed yet.");
            return;
        }
        System.out.println("\n-- Remaining participants --");
        if (leftovers.isEmpty()) {
            System.out.println("Everyone was placed in a team!");
        } else {
            for (Participant p : leftovers) {                      // 6.2
                System.out.println("|ID: "+p.getId()+ " |Name: " +p.getName()+ " |Game: "+p.getPreferredGame()+ " |Skill: "+p.getSkillLevel()+" |Role: "+p.getPreferredRole()+" |Personality Type: "+p.getPersonalityType());
            }
        }
    }
}