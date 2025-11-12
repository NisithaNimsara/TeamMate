public record Participant(
        String id,
        String name,
        String email,
        String preferredGame,
        int skillLevel,           // 1–10
        String preferredRole,
        int personalityScore,     // 20–100
        PersonalityType personalityType
) {}
