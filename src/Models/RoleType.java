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

    public static RoleType getRoleType(int value) {
        if (value == 1){
            return RoleType.STRATEGIST;
        } else if(value == 2){
            return RoleType.ATTACKER;
        }  else if(value == 3){
            return RoleType.DEFENDER;
        }  else if(value == 4){
            return RoleType.SUPPORTER;
        }  else if(value == 5){
            return RoleType.COORDINATOR;
        } else
            return OTHER;
    }
}
