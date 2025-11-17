package Models;

public enum GameType {
    CHESS,
    FIFA,
    BASKETBALL,
    CSGO,
    DOTA2,
    VALORANT,
    OTHER;

    public static GameType fromString(String value) {
        if (value == null)
            return OTHER;
        try {
            return GameType.valueOf(value.trim().toUpperCase());
        }  catch (IllegalArgumentException e) {
            return OTHER;
        }
    }

}
