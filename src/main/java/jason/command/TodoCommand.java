package jason.command;

import jason.exception.EmptyException;
import jason.model.TaskList;
import jason.model.Todo;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to add a todo task.
 */
public class TodoCommand extends Command {
    private final String description;

    /**
     * Constructs a TodoCommand.
     *
     * @param description the task description
     */
    public TodoCommand(String description) {
        this.description = description == null ? "" : description.trim();
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (description.isEmpty()) {
            throw new EmptyException();
        }
        Todo t = new Todo(description);
        tasks.add(t);
        ui.showAdd(t, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
