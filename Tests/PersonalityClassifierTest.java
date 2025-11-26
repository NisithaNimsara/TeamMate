import Models.PersonalityClassifier;
import Models.PersonalityType;
import ValidatorHelp.InvalidParticipantException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersonalityClassifierTest {

    @Test
    void testLeaderClassification() throws InvalidParticipantException {
        PersonalityClassifier classifier = new PersonalityClassifier();

        // 5 + 5 + 5 + 5 + 5 = 25. 25 * 4 = 100.
        // Score 100 should be LEADER.
        int score = classifier.calculateScore(5, 5, 5, 5, 5);
        PersonalityType type = classifier.classify(score);

        assertEquals(100, score);
        assertEquals(PersonalityType.LEADER, type);
    }

    @Test
    void testInvalidInputThrowsException() {
        PersonalityClassifier classifier = new PersonalityClassifier();

        // Input 6 is invalid (must be 1-5).
        // This test passes if the Exception IS thrown.
        assertThrows(InvalidParticipantException.class, () -> {
            classifier.calculateScore(6, 1, 1, 1, 1);
        });
    }
}