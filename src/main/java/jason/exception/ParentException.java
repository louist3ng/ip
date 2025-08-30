package jason.exception;

/**
 * Base class for all custom exceptions in the application.
 */
public class ParentException extends RuntimeException {

    /**
     * Constructor for ParentException.
     */
    public ParentException(String message) {
        super(message);
    }

    /**
     * Constructor for ParentException with a cause.
     */
    public ParentException(String message, Throwable cause) {
        super(message, cause);
    }
}