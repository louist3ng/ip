package jason.exception;

/**
 * Exception thrown when the user input is incorrect.
 */
public class IncorrectInputException extends ParentException {
    public IncorrectInputException() {
        super("No such command exists. Try again");
    }

}