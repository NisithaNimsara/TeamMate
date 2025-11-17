package Models;

public enum PersonalityType {
    LEADER,
    BALANCED,
    THINKER;

    public static PersonalityType fromString(String value){
        if(value == null)
            return null;
        try{
            return PersonalityType.valueOf(value.trim().toUpperCase());
        } catch(IllegalArgumentException e){
            return null;
        }
    }
}
