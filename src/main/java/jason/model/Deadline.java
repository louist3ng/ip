package jason.model;

import java.time.LocalDateTime;

import jason.parser.DateTimeUtil;

/**
 * Represents a deadline task with a specific time.
 */
public class Deadline extends Task {
    protected LocalDateTime time; 
    
    /**
     * Constructs a Deadline task with the given description and time.
     *
     * @param description The description of the deadline task.
     * @param time The time by which the task is due.
     */
    public Deadline(String description, LocalDateTime time) {
        super(description);
        this.time = time;
    }

    /**
     * Returns the time by which the task is due.
     *
     * @return The time by which the task is due.
     */
    public LocalDateTime getTime() {
        return time;
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
