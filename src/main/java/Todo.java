public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String getDescription() {
         return (isDone ? "[T][X] " : "[T][ ] ") + description;
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + esc(description) + " |";
    }

   
}