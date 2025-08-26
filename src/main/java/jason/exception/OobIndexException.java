package jason.exception;

public class OobIndexException extends ParentException {
    public OobIndexException() {
        super("No such task exists!");
    }
}