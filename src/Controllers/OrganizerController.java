package Controllers;

import Models.*;
import ValidatorHelp.ConsoleInput;
import java.io.*;
import java.util.List;

public class OrganizerController {
    private final ParticipantRepository repo;
    private final TeamBuilder builder;
    private final ConsoleInput input;
    private List<Team> currentTeams;
    private List<Participant> leftovers;

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

    private void importCSV() {
        String file = input.readLine("Enter Filename(without extension): ");
        try {
            int[] res = repo.importExternalFile(file+".csv");
            System.out.printf("\nImported %d participants, ignored %d.", res[0], res[1]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void formTeams() {
        System.out.println("\n-- Team Formation --");
        int size = input.readInt("Enter Team Size (2-10): ", 2, 10);

        TeamFormationThread t = new TeamFormationThread(builder, repo.getAll(), size);
        t.start();
        try {
            t.join();
            if (t.getError() != null) throw t.getError();

            TeamFormationResult result = t.getResult();

            this.currentTeams = result.getTeams();
            this.leftovers = result.getLeftovers();

            if (currentTeams.isEmpty()) {
                System.out.println("Could not form any valid teams (Constraints likely too strict or lack of Leaders).");
            } else {
                System.out.println("Success! Formed " + currentTeams.size() + " teams.");
                System.out.println("Remaining participants: " + leftovers.size());
            }

        } catch (Exception e) {
            System.out.println("Team Formation failed: " + e.getMessage());
        }
    }

    private void viewAndExport() {
        if (currentTeams == null) {
            System.out.println("No teams formed yet.");
            return;
        }
        System.out.println();
        currentTeams.forEach(System.out::println);

        if (input.readInt("Export to CSV? (1=Yes, 0=No): ", 0, 1) == 1) {
            String file = input.readLine("Enter export file name(without extension): ");

            try (PrintWriter writer = new PrintWriter(new FileWriter(file+".csv"))) {
                writer.println("TeamID,ID,Name,Email,Game,Skill,Role,Score,Type");
                for (Team t : currentTeams) {
                    for (Participant p : t.getMembers()) {
                        writer.println(t.toCSVRow(p));
                    }
                }
                System.out.println("\nTeams exported Successfully to "+file+".csv");
            } catch (IOException e) {
                System.out.println("Export failed.");
            }
        }
    }

    private void viewRemaining(){
        if (currentTeams == null){
            System.out.println("No teams formed yet.");
            return;
        }
        System.out.println("\n-- Remaining participants --");
        if (leftovers.isEmpty()) {
            System.out.println("Everyone was placed in a team!");
        } else {
            for (Participant p : leftovers) {
                System.out.println("|ID: "+p.getId()+ " |Name: " +p.getName()+ " |Game: "+p.getPreferredGame()+ " |Skill: "+p.getSkillLevel()+" |Role: "+p.getPreferredRole()+" |Personality Type: "+p.getPersonalityType());
            }
        }
    }
}