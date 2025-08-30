package jason.ui;

import jason.model.Task;
import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "─".repeat(50);

    private final Scanner in;

    public Ui() {
        this.in = new Scanner(System.in);
    }

    /* ---------- Basic framing ---------- */

    public void intro() {
        println(LINE);
        println("Hello, my name is Jason");
        println(LINE);
    }

    public void bye() {
        println(LINE);
        println("Bye. Hope to see you again soon!");
        println(LINE);
    }

    public String readInput() {
        return in.nextLine();
    }

    /**
     * Call at the very end of main loop.
     */
    public void close() {
        in.close();
    }

    public void println(String msg) {
        System.out.println(msg);
    }

    public void error(String msg) {
        System.err.println("ERROR: " + msg);
    }

    public void warn(String msg) {
        System.err.println("WARNING: " + msg);
    }

    public void line() {
        System.out.println(LINE);
    }

    /* ---------- Task-centric helpers ---------- */

    public void showList(List<Task> tasks) {
        line();
        if (tasks.isEmpty()) {
            println("No tasks yet");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, tasks.get(i).getDescription());
            }
        }
        line();
    }

    public void showAdd(Task t, int newCount) {
        line();
        println("Got it. I've added this task:");
        println("  " + t.getDescription());
        System.out.printf("Now you have %d tasks in the list.%n", newCount);
        line();
    }

    public void showDelete(Task t, int newCount) {
        line();
        println("Noted. I've removed this task:");
        println("  " + t.getDescription());
        System.out.printf("Now you have %d tasks in the list.%n", newCount);
        line();
    }

    public void showMark(Task t) {
        line();
        println("Nice! I've marked this task as done:");
        println("  " + t.getDescription());
        line();
    }

    public void showUnmark(Task t) {
        line();
        println("OK, I've marked this task as not done yet:");
        println("  " + t.getDescription());
        line();
    }

    public void showParseError(String message) {
        error(message);
    }

    public void showDiskError(String message) {
        error("Disk error: " + message);
    }
}
