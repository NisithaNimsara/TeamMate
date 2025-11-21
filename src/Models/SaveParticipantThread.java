package Models;

import ValidatorHelp.FileProcessingException;

// This thread is responsible for saving ONE participant in the background.
public class SaveParticipantThread extends Thread {

    private final ParticipantRepository repository;
    private final Participant participant;
    private FileProcessingException error;

    //constructor
    public SaveParticipantThread(ParticipantRepository repository, Participant participant) {
        this.repository = repository;
        this.participant = participant;
    }

    // This runs in a separate background thread when start() is called.
    @Override
    public void run() {
        try {
            repository.addParticipant(participant);  // save to list + CSV
        } catch (FileProcessingException e) {
            // store the error so controller can check it later
            error = e;
        }
    }
}
