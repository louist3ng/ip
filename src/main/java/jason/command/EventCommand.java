package jason.command;

import jason.exception.EmptyException;
import jason.model.Event;
import jason.model.TaskList;
import jason.parser.DateTimeUtil;
import jason.storage.Storage;
import jason.ui.Ui;
import java.time.LocalDateTime;


/**
 * Command to add an event task.
 */
public class EventCommand extends Command {
    private final String desc;
    private final String fromStr;
    private final String toStr;

    /**
     * Constructs an EventCommand.
     *
     * @param desc the task description
     * @param fromStr the start time string
     * @param toStr the end time string
     */
    public EventCommand(String desc, String fromStr, String toStr) {
        this.desc = desc == null ? "" : desc.trim();
        this.fromStr = fromStr == null ? "" : fromStr.trim();
        this.toStr = toStr == null ? "" : toStr.trim();
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) throws Exception {
        if (desc.isEmpty() || fromStr.isEmpty() || toStr.isEmpty()) {
            throw new EmptyException();
        }

        LocalDateTime from = DateTimeUtil
                .parseDayMonthYearWithTime(fromStr, DateTimeUtil.PREFER_DMY);
        LocalDateTime to = LocalDateTime
                .of(from.toLocalDate(), DateTimeUtil.parseTimeHm(toStr));
                
        if (to.isBefore(from)) {
            to = to.plusDays(1);
        }

        Event ev = new Event(desc, from, to);
        tasks.add(ev);
        ui.showAdd(ev, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
