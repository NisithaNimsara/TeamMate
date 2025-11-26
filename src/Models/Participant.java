package Models;

// This class represents a Participant.
// Knows how to store data and convert it to CSV.
public class Participant {
    private final String id;
    private final String name;
    private final String email;
    private final GameType preferredGame;
    private final int skillLevel;
    private final RoleType preferredRole;
    private final int personalityScore;
    private final PersonalityType personalityType;

    //constructor
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

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public GameType getPreferredGame() { return preferredGame; }
    public int getSkillLevel() { return skillLevel; }
    public RoleType getPreferredRole() { return preferredRole; }
    public int getPersonalityScore() { return personalityScore; }
    public PersonalityType getPersonalityType() { return personalityType; }

    // Parse one CSV line into a Participant object.
    public static Participant parseLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 8)
            return null;

        // Skip header line
        if (parts[0].equalsIgnoreCase("ID"))
            return null;

        try {
            String id = parts[0].trim();
            String name = parts[1].trim();
            String email = parts[2].trim();
            GameType game = GameType.fromString(parts[3]);
            int skill = Integer.parseInt(parts[4].trim());
            RoleType role = RoleType.fromString(parts[5]);
            int score = Integer.parseInt(parts[6].trim());
            PersonalityType type = PersonalityType.fromString(parts[7]);

            if (type == null)
                type = PersonalityType.BALANCED; //handle to not get crash

            return new Participant(id, name, email, game, skill, role, score, type);
        } catch (Exception e) {
            return null; // Return null if parsing fail
        }
    }

    // Convert into a csv compatible row
    public String toCSVRow() {
        return String.join(",",
                id,
                name,
                email,
                preferredGame.name(),
                String.valueOf(skillLevel),
                preferredRole.name(),
                String.valueOf(personalityScore),
                personalityType.name());
    }

    // To display participant data clearly in console output.
    @Override
    public String toString() {
        return "ID: " + getId() +
                " | Name: " + getName() +
                " | Email: " + getEmail() +
                " | Game: " + getPreferredGame() +
                " | Skill: " + getSkillLevel() +
                " | Role: " + getPreferredRole() +
                " | PersonalityScore: " + getPersonalityScore() +
                " | PersonalityType: " + getPersonalityType();
    }
}