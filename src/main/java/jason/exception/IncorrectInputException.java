package jason.exception;

public class IncorrectInputException extends ParentException {
    public IncorrectInputException() {
        super("No such command exists. Try again");
    }

}