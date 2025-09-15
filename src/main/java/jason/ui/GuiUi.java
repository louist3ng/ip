package jason.ui;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import jason.model.Task;

/**
 * GUI-aware Ui: sends all messages to a sink (e.g., MainWindow -> DialogBox),
 * instead of printing to System.out/err.
 */
public class GuiUi extends Ui {
    private static final String LINE = "─".repeat(50);

    private final Consumer<String> sink;

    /**
     * Creates a GUI Ui that sends all output to the given sink.
     *
     * @param sink a Consumer that accepts strings to display
     * @throws NullPointerException if sink is null
     */
    public GuiUi(Consumer<String> sink) {
        this.sink = Objects.requireNonNull(sink, "sink");
    }

    /* ---------- low-level helpers ---------- */
    private void out(String s) {
        sink.accept(s);
    }

    /* ---------- overrides to mirror Ui behavior ---------- */

    @Override
    public void showMessage(String msg) {
        out(msg);
    }

    @Override
    public void showError(String msg) {
        out(">_< " + msg);
    }

    @Override
    public void warn(String msg) {
        out("-_- " + msg);
    }

    @Override
    public void line() {
        out(LINE);
    }

    @Override
    public void showList(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            out("There’s nothing here… n-not that I’m disappointed or anything");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                sb.append(String.format("%d. %s%n", i + 1, t.getDescription()));
            }
            out(sb.toString().trim()); // print all at once
        }
    }

    @Override
    public void showAdd(Task t, int newCount) {
        String message = String.format(
                "Ugh, fine. I added this todo:\n  %s\nNow you have %d tasks in the list."
                        + "\n(You better get to work!)",
                t.getDescription(),
                newCount);
        out(message); // print once
    }

    @Override
    public void showDelete(Task t, int newCount) {
        String message = String.format(
                "Fine, I removed\n  %s\nYou have %d left. Don't slack off now!",
                t.getDescription(),
                newCount);
        out(message);
    }

    @Override
    public void showMark(Task t) {
        String message = String.format(
                "Huh? You actually finished:\n  %s"
                        + "\nW-well… good job, I guess",
                t.getDescription());
        out(message);
    }

    @Override
    public void showUnmark(Task t) {
        String message = String.format(
                "I’ve set:\n  %s\nas not done. Don’t make this a habit",
                t.getDescription());
        out(message);
    }

    @Override
    public void showFind(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("These matched. Be grateful I even looked:\n");

        if (tasks != null && !tasks.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                sb.append(String.format("%d. %s%n", i + 1, t.getDescription()));
            }
        } else {
            sb.append("I couldn’t find anything. Maybe try a smarter keyword… baka");
        }

        out(sb.toString().trim()); // single output → one chat bubble
    }

    @Override
    public void showTaskNotFound(String message) {
        showError("Task not found: " + message);
    }

    @Override
    public void showParseError(String message) {
        showError(message);
    }

    @Override
    public void showDiskError(String message) {
        showError("Disk error: " + message);
    }

}
