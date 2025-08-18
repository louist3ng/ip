public class Deadline extends Task {
    
    public Deadline(String description) {
        super(description);
    }

    @Override
    public String getDescription() {
         return (isDone ? "[D][X] " : "[D][ ] ") + description;
    }

   
}