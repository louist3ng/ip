package jason.model;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    public String getDescription() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

    public Task mark() {
        isDone = true;
        return this;
    }

    public Task unmark() {
        isDone = false;
        return this;
    }

    public abstract String toStorageString();

}
