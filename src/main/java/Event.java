public class Event extends Task {

    public Event(String description) {
        super(description);
    }

    @Override
    public String getDescription() {
         return (isDone ? "[E][X] " : "[E][ ] ") + description;
    }


}