package Model;

public enum PersonalityType {
    // personality categories based on a score
    Leader, Balanced, Thinker, Normal;

    //Convert a 0–100 score into a personality type.
    public static PersonalityType fromScaledScore(int scaled0to100) {
        if (scaled0to100 >= 90)
            return Leader;
        if (scaled0to100 >= 70 )
            return Balanced;
        if (scaled0to100 >= 50)
            return Thinker;
        else
            return Normal;
    }
}