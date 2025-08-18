public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    public String getDescription() {
        return "[ ] " + description;
    }

    public String markDescription() {
        isDone = true;
        return "[X] " + description;
    }

    public String unmarkDescription() {
        isDone = false;
        return "[ ] " + description;
    }

}