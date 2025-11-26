package ValidatorHelp;
import java.util.Scanner;

//this class handles all the user inputs from the console.
public class ConsoleInput {
    private final Scanner scanner;

    //constructor
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    //this reads String inputs and avoid and unacceptable inputs
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

    //this reads Integer inputs and avoid and unacceptable inputs
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max)
                    return val;
                //else
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}