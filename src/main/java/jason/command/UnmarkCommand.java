package jason.command;

import jason.exception.OobIndexException;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to unmark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int zeroBasedIndex;

    /**
     * Constructs an UnmarkCommand.
     *
     * @param oneBasedIndex the one-based index of the task to unmark
     */
    public UnmarkCommand(int oneBasedIndex) {
        this.zeroBasedIndex = oneBasedIndex - 1;
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new OobIndexException();
        }
        tasks.unmark(zeroBasedIndex);
        ui.showUnmark(tasks.get(zeroBasedIndex));
        storage.save(tasks.asArrayList());
    }
}
