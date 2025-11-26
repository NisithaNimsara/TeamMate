package ValidatorHelp;

//constructor, this create custom exception used to report any invalid participant data.
public class InvalidParticipantException extends Exception {
    public InvalidParticipantException(String message) {
        super(message);
    }
}