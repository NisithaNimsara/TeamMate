package Models;

import java.util.*;

//this clas represents a(one) formed team.
public class Team {
    private final int teamId;//unique team ID
    private final List<Participant> members = new ArrayList<>(); //team members

    //constructor
    public Team(int teamId) {
        this.teamId = teamId;
    }

    // add one participant to the team
    public void addMember(Participant p) {
        members.add(p);
    }

    //this will return unmodifiable members
    public List<Participant> getMembers() {
        return Collections.unmodifiableList(members);
    }

    //calculates average skill of everyone in the team
    public double getAverageSkill() {
        return members.stream().mapToInt(Participant::getSkillLevel).average().orElse(0.0);
    }

    //Helpers for the TeamBuilder logic
    //checks how many people prefer a specific game
    public long getGameCount(GameType type) {
        return members.stream().filter(p -> p.getPreferredGame() == type).count();
    }

    //checks how many people prefer a specific role
    public long getRoleCount(RoleType type) {
        return members.stream().filter(p -> p.getPreferredRole() == type).count();
    }

    //checks how many people have a specific personality
    public long getPersonalityCount(PersonalityType type) {
        return members.stream().filter(p -> p.getPersonalityType() == type).count();
    }

    //Formats data for Export
    public String toCSVRow(Participant p) {
        return teamId + "," + p.toCSVRow();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nTeamId: ").append(teamId)
                .append(" | Size: ").append(members.size()).
                append(" | AvgSkill: ").append(String.format(Locale.US,"%.2f",getAverageSkill()))
                .append("\nMembers: ");

        for (Participant p : members) {
            sb.append("\n - ").append(p);
        }
        return sb.toString();
    }
}