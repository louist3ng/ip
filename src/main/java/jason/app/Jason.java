package jason.app;

import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;
import jason.exception.OobIndexException;
import jason.model.Deadline;
import jason.model.Event;
import jason.model.Task;
import jason.model.TaskList;
import jason.model.Todo;
import jason.parser.DateTimeUtil;
import jason.parser.Parser;
import jason.storage.Storage;
import jason.ui.Ui;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Jason {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage("data/jason.txt");
    private final TaskList tasks = new TaskList();

    private void saveNow() {
        try {
            storage.save(tasks.asArrayList());
        } catch (IOException e) {
            ui.error("Failed to save tasks: " + e.getMessage());
        }
    }

    private void loadNow() {
        try {
            List<Task> loaded = storage.load();
            loaded.forEach(tasks::add);
            if (!loaded.isEmpty()) {
                ui.println(String.format("[Loaded %d tasks from disk]", loaded.size()));
            }
        } catch (IOException e) {
            ui.warn("Could not load tasks: " + e.getMessage());
        }
    }

    public void run() {
        loadNow();
        ui.intro();

        boolean exit = false;
        while (!exit) {
            String input = ui.readInput();
            String cmd = Parser.taskCommand(input);
            String contents = Parser.taskContent(input);

            try {
                if (cmd.equals("bye") && contents.isEmpty()) {
                    ui.bye();
                    exit = true;

                } else if (cmd.equals("list") && contents.isEmpty()) {
                    ui.showList(tasks.asArrayList());

                } else if (cmd.equals("todo")) {
                    if (contents.isEmpty())
                        throw new EmptyException();
                    Todo t = new Todo(contents);
                    tasks.add(t);
                    ui.showAdd(t, tasks.size());
                    saveNow();

                } else if (cmd.equals("deadline")) {
                    if (contents.isEmpty() || !contents.toLowerCase().contains("/by"))
                        throw new IncorrectInputException();
                    String[] parts = contents.split("/by", 2);
                    String desc = parts[0].trim();
                    String byStr = parts.length > 1 ? parts[1].trim() : "";
                    if (desc.isEmpty() || byStr.isEmpty())
                        throw new EmptyException();

                    LocalDateTime by = DateTimeUtil.parseIsoDateOrDateTime(byStr);
                    Deadline d = new Deadline(desc, by);
                    tasks.add(d);
                    ui.showAdd(d, tasks.size());
                    saveNow();

                } else if (cmd.equals("event")) {
                    if (contents.isEmpty())
                        throw new EmptyException();
                    String lower = contents.toLowerCase();
                    int fromIdx = lower.indexOf("/from");
                    int toIdx = lower.indexOf("/to", Math.max(fromIdx, 0) + 5);
                    if (fromIdx < 0 || toIdx < 0)
                        throw new IncorrectInputException();

                    String desc = contents.substring(0, fromIdx).trim();
                    String fromS = contents.substring(fromIdx + 5, toIdx).trim(); // "/from"
                    String toS = contents.substring(toIdx + 3).trim(); // "/to"
                    if (desc.isEmpty() || fromS.isEmpty() || toS.isEmpty())
                        throw new EmptyException();

                    LocalDateTime from = DateTimeUtil.parseDayMonthYearWithTime(fromS, DateTimeUtil.PREFER_DMY);
                    LocalDateTime to = LocalDateTime.of(from.toLocalDate(), DateTimeUtil.parseTimeHm(toS));
                    if (to.isBefore(from))
                        to = to.plusDays(1);

                    Event ev = new Event(desc, from, to);
                    tasks.add(ev);
                    ui.showAdd(ev, tasks.size());
                    saveNow();

                } else if (cmd.equals("mark")) {
                    if (contents.isEmpty())
                        throw new EmptyException();
                    int idx = Integer.parseInt(contents.replaceAll("\\s+", "")) - 1;
                    if (idx < 0 || idx >= tasks.size())
                        throw new OobIndexException();
                    tasks.mark(idx);
                    ui.showMark(tasks.get(idx));
                    saveNow();

                } else if (cmd.equals("unmark")) {
                    if (contents.isEmpty())
                        throw new EmptyException();
                    int idx = Integer.parseInt(contents.replaceAll("\\s+", "")) - 1;
                    if (idx < 0 || idx >= tasks.size())
                        throw new OobIndexException();
                    tasks.unmark(idx);
                    ui.showUnmark(tasks.get(idx));
                    saveNow();

                } else if (cmd.equals("delete")) {
                    if (contents.isEmpty())
                        throw new EmptyException();
                    int idx = Integer.parseInt(contents.replaceAll("\\s+", "")) - 1;
                    if (idx < 0 || idx >= tasks.size())
                        throw new OobIndexException();
                    Task removed = tasks.removeAt(idx);
                    ui.showDelete(removed, tasks.size());
                    saveNow();

                } else {
                    throw new IncorrectInputException();
                }

            } catch (EmptyException | OobIndexException | IncorrectInputException e) {
                ui.println(e.toString());
            } catch (NumberFormatException e) {
                ui.println("☹ OOPS!!! The task number should be an integer.");
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new Jason().run();
    }
}
