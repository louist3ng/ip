package jason.command;

import jason.exception.OobIndexException;
import jason.model.Task;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private final int zeroBasedIndex;

    /**
     * Constructs a DeleteCommand.
     *
     * @param oneBasedIndex the one-based index of the task to delete
     */
    public DeleteCommand(int oneBasedIndex) {
        this.zeroBasedIndex = oneBasedIndex - 1;
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new OobIndexException();
        }
        Task removed = tasks.removeAt(zeroBasedIndex);
        ui.showDelete(removed, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
