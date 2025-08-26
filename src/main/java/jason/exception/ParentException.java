package jason.exception;

public class ParentException extends RuntimeException {
    public ParentException(String message) { 
        super(message); 
    }

    public ParentException(String message, Throwable cause) {
        super(message, cause);
    }
}