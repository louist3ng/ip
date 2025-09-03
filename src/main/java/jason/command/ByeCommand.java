package jason.command;

import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Command to say goodbye.
 */
public class ByeCommand extends Command {
    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) {
        ui.bye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
