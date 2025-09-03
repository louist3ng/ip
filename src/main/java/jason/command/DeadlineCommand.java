package jason.command;

import jason.exception.EmptyException;
import jason.model.Deadline;
import jason.model.TaskList;
import jason.parser.DateTimeUtil;
import jason.storage.Storage;
import jason.ui.Ui;
import java.time.LocalDateTime;


/**
 * Command to add a deadline task.
 */
public class DeadlineCommand extends Command {
    private final String desc;
    private final String byStr;

    /**
     * Constructs a DeadlineCommand.
     *
     * @param desc the task description
     * @param byStr the deadline string
     */
    public DeadlineCommand(String desc, String byStr) {
        this.desc = desc == null ? "" : desc.trim();
        this.byStr = byStr == null ? "" : byStr.trim();
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (desc.isEmpty() || byStr.isEmpty()) {
            throw new EmptyException();
        }
        LocalDateTime by = DateTimeUtil.parseIsoDateOrDateTime(byStr);
        Deadline d = new Deadline(desc, by);
        tasks.add(d);
        ui.showAdd(d, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
