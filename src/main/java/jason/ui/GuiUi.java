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

    private void outf(String fmt, Object... args) {
        out(String.format(fmt, args));
    }

    /* ---------- overrides to mirror Ui behavior ---------- */

    @Override
    public void showMessage(String msg) {
        out(msg);
    }

    @Override
    public void showError(String msg) {
        out("ERROR: " + msg);
    }

    @Override
    public void warn(String msg) {
        out("WARNING: " + msg);
    }

    @Override
    public void line() {
        out(LINE);
    }

    @Override
    public void showList(List<Task> tasks) {
        line();
        if (tasks == null || tasks.isEmpty()) {
            out("No tasks yet");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                sb.append(String.format("%d. %s%n", i + 1, t.getDescription()));
            }
            out(sb.toString().trim()); // print all at once
        }
        line();
    }

    @Override
    public void showAdd(Task t, int newCount) {
        line();
        out("Got it. I've added this task:");
        out("  " + t.getDescription());
        outf("Now you have %d tasks in the list.", newCount);
        line();
    }

    @Override
    public void showDelete(Task t, int newCount) {
        line();
        out("Noted. I've removed this task:");
        out("  " + t.getDescription());
        outf("Now you have %d tasks in the list.", newCount);
        line();
    }

    @Override
    public void showMark(Task t) {
        line();
        out("Nice! I've marked this task as done:");
        out("  " + t.getDescription());
        line();
    }

    @Override
    public void showUnmark(Task t) {
        line();
        out("OK, I've marked this task as not done yet:");
        out("  " + t.getDescription());
        line();
    }

    @Override
    public void showFind(List<Task> tasks) {
        line();
        out("Here are the matching tasks in your list:");
        if (tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                outf("%d. %s", i + 1, t.getDescription());
            }
        }
        line();
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

    /*
     * ---------- input / lifecycle ----------
     * For GUI mode we don't read from Scanner or close it here;
     * those are handled by MainWindow. No override needed.
     */
}
