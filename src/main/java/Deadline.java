import java.time.LocalDateTime;

public class Deadline extends Task {
    protected LocalDateTime time;

    public Deadline(String description, LocalDateTime time) {
        super(description);
        this.time = time;
    }

    @Override
    public String getDescription() {
         return (isDone ? "[D][X] " : "[D][ ] ") + description + " (by: " + DateTimeUtil.formatHuman(time) + ")";
    }

    @Override
    public String toStorageString() {
        // D | 0 | description | by
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + DateTimeUtil.formatIsoWithSpace(time);
    }

   
}
