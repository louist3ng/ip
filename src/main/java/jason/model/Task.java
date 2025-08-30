package jason.model;


/**
 * Represents a task with a description and completion status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    public String getDescription() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

    /**
     * Marks the task as done.
     *
     * @return The updated task.
     */
    public Task mark() {
        isDone = true;
        return this;
    }

    /**
     * Marks the task as not done.
     *
     * @return The updated task.
     */
    public Task unmark() {
        isDone = false;
        return this;
    }

    /**
     * Returns whether the task is done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    public abstract String toStorageString();

}
