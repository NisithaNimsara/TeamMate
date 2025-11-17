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
        try{
            return RoleType.valueOf(value.trim().toUpperCase());
        } catch(IllegalArgumentException e){
            return OTHER;
        }
    }
}
