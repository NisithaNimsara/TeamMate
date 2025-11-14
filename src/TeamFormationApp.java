import Logic.Validators;
import Model.Participant;

import java.util.*;

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
            System.out.print("Email: ");
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
