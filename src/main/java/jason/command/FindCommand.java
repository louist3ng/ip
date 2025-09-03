package jason.command;

import jason.exception.EmptyException;
import jason.model.Task;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;
import java.util.List;


/**
 * Command to find tasks by keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand.
     *
     * @param keyword the keyword to search for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword == null ? "" : keyword.trim();
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) {
        if (keyword.isEmpty()) {
            throw new EmptyException();
        }
        List<Task> found = tasks.find(t -> t.getDescription()
                .toLowerCase().contains(keyword.toLowerCase()));
        ui.showFind(found);
        // No save needed for read-only operation
    }
}
