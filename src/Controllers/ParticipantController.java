package Controllers;


import Models.*;
import ValidatorHelp.ConsoleInput;
import ValidatorHelp.FileProcessingException;
import ValidatorHelp.InvalidParticipantException;

// This will handles everything related to "Participants".
public class ParticipantController {

    private final PersonalityClassifier classifier;
    private final ParticipantRepository repository;
    private final ConsoleInput input;

    //Constructor
    public ParticipantController(PersonalityClassifier classifier, ParticipantRepository repository, ConsoleInput input) {
        this.classifier = classifier;
        this.repository = repository;
        this.input = input;
    }

    // participant menu
    public void participantMenu(ConsoleInput input){
        boolean running = true;

        while(running){
            System.out.println("\n--- Participant Menu ---");
            System.out.println("1. New registration");
            System.out.println("2. View my details");
            System.out.println("0. Back");

            int choice = input.readInt("Select your choice: ");

            switch (choice) {
                case 1:
                    newRegistration();
                    break;
                case 2:
                    viewDetails();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // handle new registration of participant
    private void newRegistration(){
        try {
            //name
            String name = input.readLine("Enter your name: ");
            //email
            String email;
            while (true) {
                email = input.readLine("Enter your university email: ");
                boolean validEmail = repository.isValidEmail(email);
                boolean emailTaken = repository.isEmailTaken(email);
                if (!validEmail) {
                    System.out.println("Incorrect email format. Please try again.");
                } else if (emailTaken) {
                    System.out.println("This email is already registered. Try another one.");
                } else {
                    break;
                }
            }
            // preferred game
            System.out.println("\nSelect Preferred Game: ");
            System.out.println("1. CHESS");
            System.out.println("2. FIFA");
            System.out.println("3. BASKETBALL");
            System.out.println("4. CS:GO");
            System.out.println("5. DOTA 2");
            System.out.println("6. VALORANT");
            int choiceG = input.readIntInRange("Choice: ", 1, 6);
            GameType game = GameType.getGameType(choiceG);

            // skill
            int skill = input.readIntInRange("Enter your skill level (1–10): ", 1, 10);
            // preferred role
            System.out.println("\nSelect preferred Role: ");
            System.out.println("1. STRATEGIST");
            System.out.println("2. ATTACKER");
            System.out.println("3. DEFENDER");
            System.out.println("4. SUPPORTER");
            System.out.println("5. COORDINATOR");
            int choiceR = input.readIntInRange("Choice", 1, 5);
            RoleType role = RoleType.getRoleType(choiceR);

            //--------personality survey----------
            // personality score
            System.out.println("\nAnswer the following 5 questions: ");
            System.out.println(" (1= Strongly disagree .. 5=Strongly agree)");

            int q1 = input.readIntInRange("I enjoy taking the lead and guiding others during group activities.: ", 1, 5);
            int q2 = input.readIntInRange("I prefer analyzing situations and coming up with strategic solutions.: ", 1, 5);
            int q3 = input.readIntInRange("I work well with others and enjoy collaborative teamwork.: ", 1, 5);
            int q4 = input.readIntInRange("I am calm under pressure and can help maintain team morale.: ", 1, 5);
            int q5 = input.readIntInRange("I like making quick decisions and adapting in dynamic situations.: ", 1, 5);

            int score = classifier.calculateScore(q1, q2, q3, q4, q5);
            // personality type
            PersonalityType personalityType = classifier.classify(score);

            // generate id(next id)
            String id = repository.getNextId();

            //create Participant object
            Participant participant = new Participant(id, name, email, game, skill, role, score, personalityType);

            SaveParticipantThread t = new SaveParticipantThread(repository, participant);

            t.start(); // run in background
            t.join();  // wait until the tread finish

            System.out.println("\nRegistration of "+ participant.getName()+" with ID "+participant.getId()+ " saved successfully.");


        } catch (InvalidParticipantException e) {
            // If survey or any values are invalid
            System.out.println("Invalid survey values: " + e.getMessage());
        } catch (InterruptedException e) {
            // If thread is interrupted (rare but must handle)
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }

    // search participant by email
    private void viewDetails() {
        String email = input.readLine("Enter your email: ");

        // Check if this email exists in the system
        if (!repository.isEmailTaken(email)) {
            System.out.println("No participant found with this email.");
            return; // nothing more to do
        }

        // Search and display participant details
        repository.findByEmail(email).ifPresentOrElse(
                        p -> System.out.println(p.toString()),
                        () -> System.out.println("No participant found.") // should not happen
                );
    }

}
