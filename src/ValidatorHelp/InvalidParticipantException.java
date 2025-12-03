package ValidatorHelp;

public class InvalidParticipantException extends Exception {

    //constructor, this create custom exception used to report any invalid participant data.
    public InvalidParticipantException(String message) {
        super(message);
    }
}