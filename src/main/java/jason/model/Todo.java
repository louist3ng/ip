package jason.model;

public class Todo extends Task {

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