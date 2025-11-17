package Models;

import ValidatorHelp.InvalidParticipantException;

// This class do two things.
// 1. Calculating personality score.
// 2. Deciding PersonalityType.
public class PersonalityClassifier {

    // Calculates personality score.
    // Each question must be between 1 and 5.
    public int calculateScore(int q1, int q2, int q3, int q4, int q5)
            throws InvalidParticipantException {

        // Validate each question (must be 1 to 5)
        validateQuestionScore(q1);
        validateQuestionScore(q2);
        validateQuestionScore(q3);
        validateQuestionScore(q4);
        validateQuestionScore(q5);

        int raw = q1 + q2 + q3 + q4 + q5;  // add all 5
        return raw * 4;                    // scale to 100
    }

    // Determines the personality type from the final score.
    public PersonalityType classify(int scaledScore) {
        if (scaledScore >= 90) {
            return PersonalityType.LEADER;     // 90–100
        } else if (scaledScore >= 70) {
            return PersonalityType.BALANCED;   // 70–89
        } else if (scaledScore >= 50) {
            return PersonalityType.THINKER;    // 50–69
        }

        // If score is below 50 (*never happen),
        // safe default.
        return PersonalityType.BALANCED;
    }

    // Checks if a single question answer is within valid range.
    private void validateQuestionScore(int q) throws InvalidParticipantException {
        if (q < 1 || q > 5) {
            throw new InvalidParticipantException("Survey answers must be between 1 and 5.");
        }
    }
}