package Models;

public enum RoleType {
    STRATEGIST,
    ATTACKER,
    DEFENDER,
    SUPPORTER,
    COORDINATOR,
    OTHER;

    public static RoleType fromString(String value) {
        if (value == null)
            return OTHER;

        try {
            return RoleType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }

    public static RoleType getRoleType(int value) {
        switch (value) {
            case 1:
                return RoleType.STRATEGIST;
            case 2:
                return RoleType.ATTACKER;
            case 3:
                return RoleType.DEFENDER;
            case 4:
                return RoleType.SUPPORTER;
            case 5:
                return RoleType.COORDINATOR;
            default:
                return RoleType.OTHER;
        }
    }
}