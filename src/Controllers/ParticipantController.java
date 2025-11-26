package Controllers;

import Models.*;
import ValidatorHelp.*;
import java.util.logging.Logger;
import java.util.logging.Level;

//this class will handle everything related to "participant".
public class ParticipantController {
    private final ParticipantRepository repo;
    private final PersonalityClassifier classifier;
    private final ConsoleInput input;
    private static final Logger logger = Logger.getLogger(ParticipantController.class.getName());

    //constructor
    public ParticipantController(ParticipantRepository repo, PersonalityClassifier classifier, ConsoleInput input) {
        this.repo = repo;
        this.classifier = classifier;
        this.input = input;
    }

    public void showMenu() {
        while (true) {
            //preload Participant repo
            repo.loadSystemFile();

            System.out.println("\n--- Participant Menu ---");
            System.out.println("1. New registration");
            System.out.println("2. View my details");
            System.out.println("0. Back");

            int choice = input.readInt("Select your choice: ", 0, 2);
            if (choice == 1) register();
            if (choice == 2) checkDetails();
            else break;
        }
    }

    // handle new registration of participant
    private void register() {
        try {
            System.out.println("\n-- New Registration --");
            String name = input.readLine("Enter your name: ");
            String email = getValidEmail();

            // Gather details
            GameType game = GameType.fromInt(input.readInt("\nSelect Preferred Game: " +
                    "\n1. CHESS" +
                    "\n2. FIFA" +
                    "\n3. BASKETBALL" +
                    "\n4. CS : GO" +
                    "\n5. DOTA 2" +
                    "\n6. VALORANT" +
                    "\nChoice: ", 1, 6));

            int skill = input.readInt("Enter your skill level (1–10): ", 1, 10);
            RoleType role = RoleType.getRoleType(input.readInt("\nSelect preferred Role: " +
                    "\n1. STRATEGIST  - Focuses on tactics and planning. Keeps the bigger picture in mind during gameplay." +
                    "\n2. ATTACKER    - Frontline player. Good reflexes, offensive tactics, quick execution." +
                    "\n3. DEFENDER    - Protects and supports team stability. Good under pressure and team-focused." +
                    "\n4. SUPPORTER   - Jack-of-all-trades. Adapts roles, ensures smooth coordination." +
                    "\n5. COORDINATOR - Communication lead. Keeps the team informed and organized in real time." +
                    "\nChoice: ", 1, 5));

            // personality survey
            System.out.println("\n- Personality survey -" +
                    "\nAnswer the following 5 questions: " +
                    "\n (1= Strongly disagree .. 5=Strongly agree)");

            int score = classifier.calculateScore(
                    input.readInt("I enjoy taking the lead and guiding others during group activities.: ", 1, 5),
                    input.readInt("I prefer analyzing situations and coming up with strategic solutions.: ", 1, 5),
                    input.readInt("I work well with others and enjoy collaborative teamwork.: ", 1, 5),
                    input.readInt("I am calm under pressure and can help maintain team morale.: ", 1, 5),
                    input.readInt("I like making quick decisions and adapting in dynamic situations.: ", 1, 5)
            );

            //will return the Personality Type
            PersonalityType type = classifier.classify(score);
            if (type == PersonalityType.OTHER) {
                System.out.println("Sorry Participant, your personality score fell slightly below the the benchmark we required. Good luck for next time.");
                return;}

            System.out.println("Personality survey completed. Score: " + score + ", Type: "+ type);

            // Q. to append to csv
            if (input.readInt("\nDo you want to add this to CSV? (1=Yes, 0=No): ", 0, 1) == 1){
                //if yes
                //object crete
                Participant p = new Participant(repo.generateNextId(), name, email, game, skill, role, score, type);

                //tread object create
                SaveParticipantThread t = new SaveParticipantThread(repo, p);

                t.start();
                t.join();
                //grantee the error count = null
                if (t.getError() == null)
                    System.out.println("\nRegistration of "+ p.getName()+" with ID "+p.getId()+ " saved successfully.");
                else
                    System.out.println("Error saving: " + t.getError().getMessage());

            } else
                //if no
                System.out.println("Back to homepage.");

        } catch (Exception e) {
            logger.log(Level.SEVERE,"Registration failed: ", e.getMessage());
        }
    }
    //validate e-mail
    private String getValidEmail() {
        while (true) {
            String email = input.readLine("Enter your university email: ");
            //expected : something @ something . something
            if (!email.contains("@") && !email.contains("."))
                System.out.println("Incorrect email format. Please try again.");
            else if (repo.isEmailTaken(email))
                System.out.println("This email is already registered. Try another one.");
            else
                return email;
        }
    }


    // Search and display participant details
    private void checkDetails() {
        String email = input.readLine("\nEnter your email: ");
        repo.findByEmail(email).ifPresentOrElse(
                //if present
                System.out::println,
                //else
                () -> logger.info("No participant found with this email: "+ email)
        );
    }
}