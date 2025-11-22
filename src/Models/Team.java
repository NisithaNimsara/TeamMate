package Models;

import java.util.*;

// A Team represents one group of participants.
public class Team {
    private final int teamId;  //unique team ID
    private final List<Participant> members =  new ArrayList<>(); // team members

    //constructor
    public Team(int teamId) {
        this.teamId = teamId;
    }

    //this will return members but prevent modifications
    public List<Participant> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // add one participant to the team
    public void addMember(Participant member) {
        members.add(member);
    }

    // Calculate the average skill level of all team members
    public double getAverageSkill(){
        if(members.isEmpty()) {
            return 0;
        } else {
            return members.stream().mapToInt(Participant::getSkillLevel).average().orElse(0.0);
        }
    }

    // Count how many members prefer a certain game
    public long getGameCount(GameType gameType) {
        return members.stream().filter(p -> p.getPreferredGame() == gameType).count();
    }

    // Count how many members have a certain role
    public long getRoleCount(RoleType roleType) {
        return members.stream().filter(p -> p.getPreferredRole() == roleType).count();
    }

    // Count how many members have a certain personality type
    public long getPersonalityCount(PersonalityType personalityType) {
        return members.stream().filter(p -> p.getPersonalityType() == personalityType).count();
    }

    // Convert one participant into a CSV
    public String toCSVRow(Participant p) {
        return teamId + "," + p.toCSVRow();
    }

    // Create a readable version
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Team #").append(teamId)
                .append(" | size=").append(members.size())
                .append(" | avgSkill=")
                .append(String.format(Locale.US, "%.2f", getAverageSkill()))
                .append("\nMembers:");

        for (Participant p : members) {
            sb.append("\n  - ").append(p);
        }

        return sb.toString();
    }










}
