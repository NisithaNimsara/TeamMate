package Models;

import ValidatorHelp.FileProcessingException;


public class SaveParticipantThread extends Thread {

    private final ParticipantRepository repository;
    private final Participant participant;
    private FileProcessingException error;

    public SaveParticipantThread(ParticipantRepository repository, Participant participant) {
        this.repository = repository;
        this.participant = participant;
    }

    @Override
    public void run() {
        try {
            repository.addParticipant(participant);  // save to list + CSV
        } catch (FileProcessingException e) {
            // store the error so controller can check it later
            error = e;
        }
    }

    public FileProcessingException getError() {
        return error;
    }
}
