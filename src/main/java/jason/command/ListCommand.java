package jason.command;

import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) {
        ui.showList(tasks.asArrayList());
    }
}
