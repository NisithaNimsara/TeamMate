import File.CSV;
import Logic.Validators;
import Model.ImportResult;
import Model.Participant;
import Model.PersonalityType;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;

public class TeamFormationApp {

    // system CSV
    static final String PARTICIPANTS_CSV = "participants.csv";

    // to #memory state
    static final List<Participant> importedParticipants = new ArrayList<>();

    // email uniqueness check
    static final Set<String> knownEmails = new HashSet<>();

    // Auto-increment participant ID counter
    static int nextIdCounter = 1;

    // Check fields are empty?
    static String promptNonEmpty(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return s;
            System.out.println("Please enter a value.");
        }
    }

    // check integer fields are in assigned range?
    static int promptIntRange(Scanner sc, String label, int min, int max) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            }  catch (NumberFormatException e) {
                System.out.println("Please enter an Integer between [" + min + " and " + max + "]");
            }
        }
    }

    // Check Email's uniqueness and format
    static String promptEmailUnique(Scanner sc) {
        while (true) {
            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();
            if (!Validators.isValidEmail(email)){
                System.out.println("invalid Email format. Try again");
                continue;
            }
            String key = email.toLowerCase(Locale.ROOT);
            if (knownEmails.contains(key)) {
                System.out.println("This Email already registered. Please use different one");
                continue;
            }
            return email;
        }
    }

    // Increase ID by one with the correct format
    static String allocateNextId(){
        String id = String.format("P%03d", nextIdCounter);
        nextIdCounter++;
        return id;
    }

    // check and ensure the CSV header exist(correct)
    static void ensureCsvHeaderExist(String fileName) {
        Path p = Paths.get(fileName);
        if (!Files.exists(p)) {
            try{
                CSV.writeHeader(fileName, Arrays.asList(
                        "id","name","email","preferredGame","skillLevel","preferredRole","personalityScore","personalityType"));
            } catch (IOException e) {
                System.out.println("Could not Create "+ fileName +": "+e.getMessage());
            }
        }
    }

    // Accepts P7 / P007 / p12 (all kinds)
    static void updateIdCointerFromExisting (String id){
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^[Pg](\\d+)$").matcher(id);
        if (m.matches()){
            int n = Integer.parseInt(m.group(1));
            nextIdCounter = Math.max(nextIdCounter, n + 1);
        }
    }


    // read file and import it into memory with error handing as well
    static ImportResult importParticipants(String fileName) {
        int imported = 0,  ignored = 0;
        try(BufferedReader br = Files.newBufferedReader(Paths.get(fileName))){
            String header = br.readLine(); //Skip header
            if (header == null) return new ImportResult(0, 0);
            String line;
            while ((line = br.readLine()) != null){
                List<String> cols = CSV.parseLine(line);
                if (cols.size() != 8){ ignored++; continue; }
                try{
                    String id = cols.get(0).trim();
                    String name = cols.get(1).trim();
                    String email = cols.get(2).trim();
                    String preferredGame = cols.get(3).trim();
                    int skillLevel = Integer.parseInt(cols.get(4).trim());
                    String preferredRole = cols.get(5).trim();
                    int personalityScore = Integer.parseInt(cols.get(6).trim());
                    PersonalityType personalityType = PersonalityType.valueOf(cols.get(7).trim());

                    if (id.isEmpty() || name.isEmpty() || Validators.isValidEmail(email)){ignored++; continue; }
                    if(skillLevel < 1 || skillLevel >10 || personalityScore < 20 || personalityScore > 100){ ignored++; continue; }

                    String key = email.toLowerCase(Locale.ROOT);

                    importedParticipants.add(new Participant(id,name,email,preferredGame,skillLevel,preferredRole,personalityScore,personalityType));
                    knownEmails.add(key);

                    updateIdCointerFromExisting(id);

                    imported++;
                } catch (Exception ex){
                    ignored++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading:  "+e.getMessage());
        }
        return new ImportResult(imported, ignored);
    }

    // preload a System file
    static void preloadFromSystemFile(){
        importedParticipants.clear();
        knownEmails.clear();
        nextIdCounter = 1;

        if (Files.exists(Paths.get(PARTICIPANTS_CSV))) return;
        importParticipants(PARTICIPANTS_CSV);
    }

    // participant survey and input gathering(with validations)
    static void handleParticipant(Scanner sc){
        try{
            System.out.println("\n-- Participant Registration --");

            preloadFromSystemFile(); //Reload file

            String name = promptNonEmpty(sc, "Enter Name: ");
            String email = promptEmailUnique(sc);
            if(!Validators.isValidEmail(email)){
                System.out.println("invalid Email format. Try again");
            }
            String preferredGame = promptNonEmpty(sc, "Enter Preferred Game: ");
            int skillLevel = promptIntRange(sc, "Enter skill level (1-10): ",1,10);
            String preferredRole = promptNonEmpty(sc, "Enter preferred Role: ");

            System.out.println("\nAnswer each from 1 (Strongly Disagree) to 5 (Strongly Agree)");
            int q1 = promptIntRange(sc, "I enjoy taking the lead and guiding others during group activities: ", 1, 5);
            int q2 = promptIntRange(sc, "I prefer analyzing situations and coming up with strategic solutions: ", 1, 5);
            int q3 = promptIntRange(sc, "I work well with others and enjoy collaborative teamwork: ", 1, 5);
            int q4 = promptIntRange(sc, "I am calm under pressure and can help maintain team morale: ", 1, 5);
            int q5 = promptIntRange(sc, "I like making quick decisions and adapting in dynamic situations: ", 1, 5);

            int score = q1 + q2 + q3 + q4 + q5; // 5–25
            int scaledScore = score * 4;        // 20–100

            PersonalityType personalityType = PersonalityType.fromScaledScore(scaledScore);
            // generate next ID
            String newId = allocateNextId();

            Participant p = new Participant(newId,name,email,preferredGame,skillLevel,preferredRole,scaledScore,personalityType);

            //Write to the file
            CSV.appendRow(PARTICIPANTS_CSV, Arrays.asList(
                    p.id(), p.name(), p.email(), p.preferredGame(),
                    String.valueOf(p.skillLevel()),  p.preferredRole(),
                    String.valueOf(p.personalityScore()), p.personalityType().name()
            ));

            //update to memory
            importedParticipants.add(p);
            knownEmails.add(email.toLowerCase(Locale.ROOT));

            System.out.println("Success message: Saved " + p.name() + " with ID " + p.id() + " to " + PARTICIPANTS_CSV);
        } catch (Exception e){
            System.out.println("Failed to save participant: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n==== Main ====");
            System.out.println("1) Participant");
            System.out.println("2) Organizer");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> System.out.println("Participant");
                case "2" -> System.out.println("Organizer");
                case "0" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

}
