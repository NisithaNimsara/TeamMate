public class Participant {
    private String id;
    private String name;
    private String eMail;
    private String preffedGame;
    private int skillLevel;
    private String preffedRole;

    public Participant(String id, String name, String eMail, String preffedGame, int skillLevel, String preffedRole) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
        this.preffedGame = preffedGame;
        this.skillLevel = skillLevel;
        this.preffedRole = preffedRole;
    }
}
