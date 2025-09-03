package jason.command;

import jason.exception.OobIndexException;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final int zeroBasedIndex;

    /**
     * Constructs a MarkCommand.
     *
     * @param oneBasedIndex the one-based index of the task to mark
     */
    public MarkCommand(int oneBasedIndex) {
        this.zeroBasedIndex = oneBasedIndex - 1;
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new OobIndexException();
        }
        tasks.mark(zeroBasedIndex);
        ui.showMark(tasks.get(zeroBasedIndex));
        storage.save(tasks.asArrayList());
    }
}
