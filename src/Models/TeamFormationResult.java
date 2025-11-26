package Models;

import java.util.List;

// this class holds formed teams and leftover participants as lists
public class TeamFormationResult {
    private final List<Team> teams;
    private final List<Participant> leftovers;

    public TeamFormationResult(List<Team> teams, List<Participant> leftovers) {
        this.teams = teams;
        this.leftovers = leftovers;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Participant> getLeftovers() {
        return leftovers;
    }
}