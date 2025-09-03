package jason.command;

import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

/**
 * Abstract class representing a command.
 */
public abstract class Command {
    public abstract void execute(Ui ui, TaskList tasks, Storage storage) throws Exception;

    /**
     * Returns true if this command is an exit command.
     */
    public boolean isExit() {
        return false;   // default for all subclasses
    }
}
