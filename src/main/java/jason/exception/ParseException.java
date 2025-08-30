package jason.exception;

/**
 * Exception thrown when there is an error in parsing user input.
 */
public class ParseException extends ParentException {

    /**
     * Constructor for ParseException.
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Constructor for ParseException with a cause.
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
