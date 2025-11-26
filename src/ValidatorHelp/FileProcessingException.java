package ValidatorHelp;

public class FileProcessingException extends Exception {

    //constructor, this will crete execution with custom error message.
    public FileProcessingException(String message) {
        super(message);
    }
}