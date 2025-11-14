import java.util.*;

public class TeamFormationApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n==== Main ====");
            System.out.println("1) Model.Participant");
            System.out.println("2) Organizer");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> System.out.println("Model.Participant");
                case "2" -> System.out.println("Organizer");
                case "0" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }

    }
}
