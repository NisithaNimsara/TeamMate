public enum PersonalityType {
    Leader, Balanced, Thinker, Normal; // these types only

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