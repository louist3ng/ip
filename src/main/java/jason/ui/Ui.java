package jason.ui;

import jason.model.Task;
import java.util.List;
import java.util.Scanner;

/**
 * User interface class to handle input and output.
 */
public class Ui {
    private static final String LINE = "─".repeat(50);

    private final Scanner in;

    /**
     * Constructor for Ui.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /* ---------- Basic framing ---------- */

    /**
     * Displays the introduction message.
     */
    public void intro() {
        showMessage(LINE);
        showMessage("Hello, my name is Jason");
        showMessage(LINE);
    }

    /**
     * Displays the goodbye message.
     */
    public void bye() {
        showMessage(LINE);
        showMessage("Bye. Hope to see you again soon!");
        showMessage(LINE);
    }

    /**
     * Reads a line of input from the user.
     * @return the input string
     */
    public String readInput() {
        return in.nextLine();
    }

    /**
     * Call at the very end of main loop.
     */
    public void close() {
        in.close();
    }

    /**
     * Prints a message to the standard output.
     * @param msg the message to print
     */
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Prints an error message to the standard error output.
     * @param msg the error message to print
     */
    public void showError(String msg) {
        System.err.println("ERROR: " + msg);
    }

    /**
     * Prints a warning message to the standard error output.
     * @param msg the warning message to print
     */
    public void warn(String msg) {
        System.err.println("WARNING: " + msg);
    }

    /**
     * Prints a horizontal line to the standard output.
     */
    public void line() {
        System.out.println(LINE);
    }

    /* ---------- Task-centric helpers ---------- */

    /**
     * Displays the list of tasks.
     * @param tasks the list of tasks to display
     */
    public void showList(List<Task> tasks) {
        line();
        if (tasks.isEmpty()) {
            showMessage("No tasks yet");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, tasks.get(i).getDescription());
            }
        }
        line();
    }

    /**
     * Displays a message when a task is added.
     * @param t the task that was added
     * @param newCount the new total number of tasks
     */
    public void showAdd(Task t, int newCount) {
        line();
        showMessage("Got it. I've added this task:");
        showMessage("  " + t.getDescription());
        System.out.printf("Now you have %d tasks in the list.%n", newCount);
        line();
    }

    /**
     * Displays a message when a task is deleted.
     * @param t the task that was deleted
     * @param newCount the new total number of tasks
     */
    public void showDelete(Task t, int newCount) {
        line();
        showMessage("Noted. I've removed this task:");
        showMessage("  " + t.getDescription());
        System.out.printf("Now you have %d tasks in the list.%n", newCount);
        line();
    }

    /**
     * Displays a message when a task is marked as done.
     * @param t the task that was marked
     */
    public void showMark(Task t) {
        line();
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + t.getDescription());
        line();
    }

    /**
     * Displays a message when a task is marked as not done.
     * @param t the task that was unmarked
     */
    public void showUnmark(Task t) {
        line();
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + t.getDescription());
        line();
    }

    /**
     * Displays a list of tasks that match the given criteria.
     * @param tasks the list of tasks to display
     */
    public void showFind(List<Task> tasks) {
        line();
        showMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, tasks.get(i).getDescription());
        }
        line();
    }

    /**
     * Displays a message when a task cannot be found.
     * @param message the error message
     */
    public void showTaskNotFound(String message) {
        showError("Task not found: " + message);
    }

    /**
     * Displays a message when a parsing error occurs.
     * @param message the error message
     */
    public void showParseError(String message) {
        showError(message);
    }

    /**
     * Displays a message when a disk error occurs.
     * @param message the error message
     */
    public void showDiskError(String message) {
        showError("Disk error: " + message);
    }
}
