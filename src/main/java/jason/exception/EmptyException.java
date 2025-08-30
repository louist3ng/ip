package jason.exception;

/**
 * Exception thrown when a command is empty or not found.
 */
public class EmptyException extends ParentException {
    public EmptyException() {
        super("No such command exists!");
    }
}   