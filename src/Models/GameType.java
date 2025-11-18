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

    public static GameType getGameType(int value) {
        if (value == 1) {
            return GameType.CHESS;
        } else if (value == 2) {
            return GameType.FIFA;
        } else if (value == 3) {
            return GameType.BASKETBALL;
        } else if (value == 4) {
            return GameType.CSGO;
        } else if (value == 5) {
            return GameType.DOTA2;
        } else if (value == 6) {
            return GameType.VALORANT;
        } else {
            return GameType.OTHER;
        }
    }

}
