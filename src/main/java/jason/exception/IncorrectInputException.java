package jason.exception;

/**
 * Exception thrown when the user input is incorrect.
 */
public class IncorrectInputException extends ParentException {
    public IncorrectInputException() {
        super("What was that supposed to be? Try help before embarrassing us both");
    }

}