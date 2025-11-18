package Models;
import java.util.Objects;

// This class represents a Participant.
// This store all the information collected from the survey and CSV file.
public class Participant {
    private final String id;
    private final String name;
    private final String email;
    private final GameType preferredGame;
    private final int skillLevel;
    private final RoleType preferredRole;
    private final int personalityScore;
    private final PersonalityType personalityType;

    // Constructor
    public Participant(String id,
                       String name,
                       String email,
                       GameType preferredGame,
                       int skillLevel,
                       RoleType preferredRole,
                       int personalityScore,
                       PersonalityType personalityType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;
    }

    // Getters (only read access)
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public GameType getPreferredGame() {
        return preferredGame;
    }
    public int getSkillLevel() {
        return skillLevel;
    }
    public RoleType getPreferredRole() {
        return preferredRole;
    }
    public int getPersonalityScore() {
        return personalityScore;
    }
    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    // To display participant data clearly in console output.
    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + name +
                " | Email: " + email +
                " | Game: " + preferredGame +
                " | Skill: " + skillLevel +
                " | Role: " + preferredRole +
                " | PersonalityScore: " + personalityScore +
                " | PersonalityType: " + personalityType;
    }

    // Convert the object back into a CSV-compatible row
    public String toCSVRow() {
        return String.join(",",
                id,
                name,
                email,
                preferredGame.name(),
                String.valueOf(skillLevel),
                preferredRole.name(),
                String.valueOf(personalityScore),
                personalityType.name()
        );
    }
}
