package jason.ui;

import jason.model.Task;
import java.util.List;
import java.util.Scanner;

/**
 * User interface class to handle input and output.
 */
public class Ui {
    // ===== Constants (avoid magic literals) =====
    private static final String LINE = "─".repeat(50);
    private static final String PREFIX_ERROR = "ERROR: ";
    private static final String PREFIX_WARNING = "WARNING: ";

    private static final String MSG_HELLO = "Hello, my name is Jason";
    private static final String MSG_BYE = "Bye. Hope to see you again soon!";
    private static final String MSG_NO_TASKS = "No tasks yet";
    private static final String MSG_LIST_MATCHING = "Here are the matching tasks in your list:";
    private static final String MSG_FIND_EMPTY = "No matching tasks found.";
    private static final String MSG_ADDED = "Got it. I've added this task:";
    private static final String MSG_REMOVED = "Noted. I've removed this task:";
    private static final String MSG_MARKED = "Nice! I've marked this task as done:";
    private static final String MSG_UNMARKED = "OK, I've marked this task as not done yet:";
    private static final String MSG_COUNT_FMT = "Now you have %d tasks in the list.";

    private final Scanner in;

    /** Constructor for Ui. */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /* ---------- Basic framing ---------- */

    /** Displays the introduction message. */
    public void intro() {
        framed(() -> showMessage(MSG_HELLO));
    }

    /** Displays the goodbye message. */
    public void bye() {
        framed(() -> showMessage(MSG_BYE));
    }

    /** Reads a line of input from the user. */
    public String readInput() {
        return in.nextLine();
    }

    /** Call at the very end of main loop. */
    public void close() {
        in.close();
    }

    /* ---------- Low-level output ---------- */

    /** Prints a message to the standard output. */
    public void showMessage(String msg) {
        assert msg != null; // caller should ensure non-null
        System.out.println(msg);
    }

    /** Prints an error message to the standard error output. */
    public void showError(String msg) {
        assert msg != null; // caller should ensure non-null
        System.err.println(PREFIX_ERROR + msg);
    }

    /** Prints a warning message to the standard error output. */
    public void warn(String msg) {
        assert msg != null; // caller should ensure non-null
        System.err.println(PREFIX_WARNING + msg);
    }

    /** Prints a horizontal line to the standard output. */
    public void line() {
        System.out.println(LINE);
    }

    /* ---------- Task-centric helpers ---------- */

    /** Displays the list of tasks. */
    public void showList(List<Task> tasks) {
        assert tasks != null; // caller should ensure non-null
        framed(() -> {
            if (tasks == null || tasks.isEmpty()) {
                showMessage(MSG_NO_TASKS);
            } else {
                showMessage(renderIndexedList(tasks));
            }
        });
    }

    /** Displays a message when a task is added. */
    public void showAdd(Task t, int newCount) {
        framed(() -> {
            showMessage(MSG_ADDED);
            showMessage("  " + describe(t));
            showMessage(String.format(MSG_COUNT_FMT, newCount));
        });
    }

    /** Displays a message when a task is deleted. */
    public void showDelete(Task t, int newCount) {
        framed(() -> {
            showMessage(MSG_REMOVED);
            showMessage("  " + describe(t));
            showMessage(String.format(MSG_COUNT_FMT, newCount));
        });
    }

    /** Displays a message when a task is marked as done. */
    public void showMark(Task t) {
        framed(() -> {
            showMessage(MSG_MARKED);
            showMessage("  " + describe(t));
        });
    }

    /** Displays a message when a task is marked as not done. */
    public void showUnmark(Task t) {
        framed(() -> {
            showMessage(MSG_UNMARKED);
            showMessage("  " + describe(t));
        });
    }

    /** Displays a list of tasks that match the given criteria. */
    public void showFind(List<Task> tasks) {
        framed(() -> {
            if (tasks == null || tasks.isEmpty()) {
                showMessage(MSG_FIND_EMPTY);
            } else {
                showMessage(MSG_LIST_MATCHING);
                showMessage(renderIndexedList(tasks));
            }
        });
    }

    /** Displays a message when a task cannot be found. */
    public void showTaskNotFound(String message) {
        showError("Task not found: " + message);
    }

    /** Displays a message when a parsing error occurs. */
    public void showParseError(String message) {
        showError(message);
    }

    /** Displays a message when a disk error occurs. */
    public void showDiskError(String message) {
        showError("Disk error: " + message);
    }

    /* ---------- Private helpers (SLAP/DRY) ---------- */

    /** Wrap content with top/bottom lines for consistent framing. */
    private void framed(Runnable body) {
        body.run();
    }

    /** How a task is displayed in lists/messages (centralized). */
    protected String describe(Task t) {
        // Switch to t.toString() if you want status/date shown everywhere.
        return t.getDescription();
    }

    /** Renders an indexed list of tasks starting at 1. */
    private String renderIndexedList(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(i + 1).append(". ").append(describe(tasks.get(i)));
        }
        return sb.toString();
    }
}
