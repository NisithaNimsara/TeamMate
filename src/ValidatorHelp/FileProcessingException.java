package ValidatorHelp;

// This custom exception is used whenever something goes wrong
// while reading or writing files (CSV loading, saving, importing).
public class FileProcessingException extends Exception {

    // Constructor: create an exception with a custom error message.
    public FileProcessingException(String message) {
        super(message);
    }
}