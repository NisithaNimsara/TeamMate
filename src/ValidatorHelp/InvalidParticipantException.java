package ValidatorHelp;

//  This is a custom exception used to report any invalid participant data.
public class InvalidParticipantException extends Exception {

    // Constructor: create an exception with a simple message.
    public InvalidParticipantException(String message) {
        super(message);
    }
}