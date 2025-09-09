package jason.app;

import jason.command.Command;
import jason.command.CommandParser;
import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;
import jason.exception.OobIndexException;
import jason.model.Task;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;
import java.io.IOException;
import java.util.List;


/**
 * Main application class.
 */
public class Jason {
    // ===== Constants (avoid magic literals) =====
    private static final String DATA_FILE = "data/jason.txt";
    private static final String MSG_LOADED = "[Loaded %d tasks from disk]";
    private static final String MSG_NUM_FORMAT = "☹ OOPS!!! The task number should be an integer.";
    private static final String MSG_UNEXPECTED = "☹ OOPS!!! Something went wrong: ";

    private final Ui ui = new Ui();
    private final Storage storage = new Storage(DATA_FILE);
    private final TaskList tasks = new TaskList();

    /**
     * Loads tasks from the storage.
     */
    private void loadNow() {
        try {
            List<Task> loaded = storage.load();
            loaded.forEach(tasks::add);
            if (!loaded.isEmpty()) {
                ui.showMessage(String.format(MSG_LOADED, loaded.size()));
            }
        } catch (IOException e) {
            ui.warn("Could not load tasks: " + e.getMessage());
        }
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        loadNow();
        ui.intro();

        while (true) {
            try {
                String input = ui.readInput();
                Command command = CommandParser.parse(input);
                command.execute(ui, tasks, storage);
                if (command.isExit()) {
                    break;
                }
            } catch (EmptyException | OobIndexException | IncorrectInputException e) {
                ui.showMessage(e.toString());
            } catch (NumberFormatException e) {
                ui.showMessage(MSG_NUM_FORMAT);
            } catch (Exception e) {
                ui.showMessage(MSG_UNEXPECTED + e.getMessage());
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new Jason().run();
    }
}
