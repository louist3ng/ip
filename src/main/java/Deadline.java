public class Deadline extends Task {
    protected String time;
    
    public Deadline(String description, String time) {
        super(description);
        this.time = time;
    }

    @Override
    public String getDescription() {
         return (isDone ? "[D][X] " : "[D][ ] ") + description + " (by: " + time + ")";
    }

    @Override
    public String toFileString() {
        // D | 0 | description | by
        return "D | " + (isDone ? "1" : "0") + " | " + esc(description) + " | " + esc(this.time);
    }

   
}
