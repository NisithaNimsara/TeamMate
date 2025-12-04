package Models;

//this class calculate personality score and decide personality type
public class PersonalityClassifier {

    public int calculateScore(int q1, int q2, int q3, int q4, int q5){
        return (q1 + q2 + q3 + q4 + q5) * 4; // Scale to 100
    }

    public PersonalityType classify(int score) {
        if (score >= 90)
            return PersonalityType.LEADER;
        if (score >= 70)
            return PersonalityType.BALANCED;
        if (score >= 50)
            return PersonalityType.THINKER;
        //else
        return PersonalityType.OTHER;
    }
}