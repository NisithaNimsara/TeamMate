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
