package Models;
import ValidatorHelp.FileProcessingException;
//This thread is responsible for saving ONE participant in the background.
public class SaveParticipantThread extends Thread {
    private final ParticipantRepository repo;
    private final Participant participant;
    private Exception error;

    //constructor
    public SaveParticipantThread(ParticipantRepository repo, Participant participant) {
        this.repo = repo;
        this.participant = participant;
    }

    @Override
    public void run() {
        try {
            repo.addParticipant(participant);
        } catch (FileProcessingException e) {
            error = e;
        }
    }

    public Exception getError() {
        return error; }
}