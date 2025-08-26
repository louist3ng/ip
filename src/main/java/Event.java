public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
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
        // File format wants ONE extra field (e.g., "Aug 6th 2-4pm").
        // We'll combine from/to into a single human string when saving.
        String when = (to == null || to.isEmpty()) ? from : (from + " " + to);
        return "E | " + (isDone ? "1" : "0") + " | " + esc(description) + " | " + esc(when);
    }


}