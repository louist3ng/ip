/** Thrown when user input or storage lines cannot be parsed. */
public class ParseException extends ParentException {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
