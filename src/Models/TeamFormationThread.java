package Models;

import java.util.List;

// This class represents a dedicated Thread that will run team formation
public class TeamFormationThread extends Thread {

    private final TeamBuilder teamBuilder;                 // logic used to form teams
    private final List<Participant> participants;          // list of all participants
    private final int teamSize;                            // desired team size

    private List<Team> result;                             // will store the generated teams
    private Exception error;                               // will store any error that happens

    //constructor
    public TeamFormationThread(TeamBuilder teamBuilder, List<Participant> participants, int teamSize) {
        this.teamBuilder = teamBuilder;
        this.participants = participants;
        this.teamSize = teamSize;
    }

    // This method runs when the thread is started.
    // Heavy work (team formation) happens here, not on the main thread.
    @Override
    public void run() {
        try {
            // perform team formation
            result = teamBuilder.formTeams(participants, teamSize);
        } catch (Exception e) {
            // if anything goes wrong, store the error
            error = e;
        }
    }

    // this method will give the teams
    public List<Team> getResult() {
        return result;
    }

    // this will check if and exception occurred?
    public Exception getError() {
        return error;
    }
}
