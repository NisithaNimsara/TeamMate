package ValidatorHelp;

import java.util.Scanner;

// This class is responsible for reading all user input from the console.
public class ConsoleInput {

    private Scanner scanner;  // Scanner object used to read text

    // Constructor: the Scanner is passed in from outside.
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    // Reads a full line of text from the user.
    // Trims spaces to avoid accidental white-space issues.
    // avoid empty fields.
    public String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            try{
                String value = scanner.nextLine();
                if (value.isEmpty()) {
                    System.out.println("Field is empty");
                } else  {
                    return value;
                }
            } catch(Exception e){
                System.out.println("Invalid input");
            }
        }
    }

    // Reads an integer safely.
    // If the user types a letter or invalid number, it asks again.
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                // Read as string first, then convert to int
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                // If conversion fails, tell the user and repeat
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Reads an integer AND checks if it's within a required range.
    // Example: skill score 1–5 or team size 2–10.
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);  // get integer the safe way

            // check if it fits within the allowed range
            if (value < min || value > max) {
                System.out.printf("Please enter a value between %d and %d.%n", min, max);
            } else {
                return value; // valid number → return it
            }
        }
    }

}
