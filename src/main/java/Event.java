import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getDescription() {
         return (isDone ? "[E][X] " : "[E][ ] ")
            + description + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileString() {
        // E | 0 | description | 2019-12-02T18:00 | 2019-12-02T20:00
        return "E | " + (isDone ? "1" : "0") + " | " + esc(description)
                + " | " + esc(DateTimeUtil.formatIsoWithSpace(from))
                + " | " + esc(DateTimeUtil.formatIsoWithSpace(to));
    }


}