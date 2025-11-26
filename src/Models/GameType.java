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

        value = value.trim().toLowerCase();

        if (value.equals("chess"))
            return CHESS;
        else if (value.equals("fifa"))
            return FIFA;
        else if (value.equals("basketball"))
            return BASKETBALL;
        else if (value.equals("cs:go") || value.equals("csgo"))
            return CSGO;
        else if (value.equals("dota 2") || value.equals("dota2"))
            return DOTA2;
        else if (value.equals("valorant"))
            return VALORANT;
        else
            return OTHER;
    }

    public static GameType fromInt(int value) {
        switch (value) {
            case 1:
                return GameType.CHESS;
            case 2:
                return GameType.FIFA;
            case 3:
                return GameType.BASKETBALL;
            case 4:
                return GameType.CSGO;
            case 5:
                return GameType.DOTA2;
            case 6:
                return GameType.VALORANT;
            default:
                return GameType.OTHER;
        }
    }
}