package jason.exception;

/**
 * Exception thrown when an index is out of bounds.
 */
public class OobIndexException extends ParentException {
    public OobIndexException() {
        super("That index is outside your list. Pay attention!");
    }
}