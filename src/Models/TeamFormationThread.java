package Models;

import java.util.List;

//this class runs the team formation in a background
public class TeamFormationThread extends Thread {

    private final TeamBuilder teamBuilder;
    private final List<Participant> participants;
    private final int teamSize;

    private TeamFormationResult result;
    private Exception error;

    //constructor
    public TeamFormationThread(TeamBuilder teamBuilder, List<Participant> participants, int teamSize) {
        this.teamBuilder = teamBuilder;
        this.participants = participants;
        this.teamSize = teamSize;
    }

    @Override
    public void run() {
        try {
            result = teamBuilder.formTeams(participants, teamSize);     // 4.5.1
        } catch (Exception e) {
            error = e;
        }
    }

    //getters---------
    public TeamFormationResult getResult() {
        return result;
    }

    public Exception getError() {

        return error;
    }
}