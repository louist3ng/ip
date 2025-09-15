package jason.exception;

/**
 * Exception thrown when a command is empty or not found.
 */
public class EmptyException extends ParentException {
    public EmptyException() {
        super("Say what you mean for the command. I can’t read minds… yet");
    }
}   