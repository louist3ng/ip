package jason.model;

import java.time.LocalDateTime;

import jason.parser.DateTimeUtil;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String getDescription() {
         return (isDone ? "[E][X] " : "[E][ ] ")
            + description + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toStorageString() {
        // E | 0 | description | 2019-12-02T18:00 | 2019-12-02T20:00
        return "E | " + (isDone ? "1" : "0") + " | " + description
                + " | " + DateTimeUtil.formatIsoWithSpace(from)
                + " | " + DateTimeUtil.formatIsoWithSpace(to);
    }


}