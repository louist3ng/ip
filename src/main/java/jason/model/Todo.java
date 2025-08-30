package jason.model;

/**
 * Represents a todo task.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the given description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getDescription() {
        return (isDone ? "[T][X] " : "[T][ ] ") + description;
    }

    @Override
    public String toStorageString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

   
}