package jason.command;

import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.Ui;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Command to display help information.
 */
public class HelpCommand extends Command {
    private final String topic; // may be null/empty

    /**
     * Default constructor for HelpCommand with no specific topic.
     */
    public HelpCommand() {
        this(null);
    }

    /**
     * Constructor for HelpCommand with a specific topic.
     *
     * @param topic the topic to get help for
     */
    public HelpCommand(String topic) {
        this.topic = topic.trim().toLowerCase();
    }

    @Override
    public void execute(Ui ui, TaskList tasks, Storage storage) {
        if (topic.isEmpty()) {
            ui.showMessage(allHelpText());
        } else {
            String one = oneTopicHelp(topic);
            if (one == null) {
                ui.showError("No help found for: " + topic);
                ui.showMessage("Type `help` to see the list of commands.");
            } else {
                ui.showMessage(one);
            }
        }
    }

    /* ---------- Help content ---------- */

    private static final Map<String, HelpEntry> HELP = new LinkedHashMap<>();

    static {
        // Keep order stable with LinkedHashMap.
        HELP.put("list", new HelpEntry(
                "Show all tasks. Obviously",
                new String[]{
                        "list"
                }
        ));
        HELP.put("todo", new HelpEntry(
                "Add a to-do task.",
                new String[]{
                        "todo Buy milk"
                }
        ));
        HELP.put("deadline", new HelpEntry(
                "Add a task with a deadline (24h time).",
                new String[]{
                        "deadline CS2103T iP /by 2025-09-20 23:59"
                }
        ));
        HELP.put("event", new HelpEntry(
                "Add an event with start and end (24h time).",
                new String[]{
                        "event Meeting /from 2025-09-18 14:00 /to 2025-09-18 16:00"
                }
        ));
        HELP.put("mark", new HelpEntry(
                "Mark a task as done by its list index.",
                new String[]{
                        "mark 2"
                }
        ));
        HELP.put("unmark", new HelpEntry(
                "Unmark a task as not done.",
                new String[]{
                        "unmark 2"
                }
        ));
        HELP.put("delete", new HelpEntry(
                "Delete a task by its list index.",
                new String[]{
                        "delete 3"
                }
        ));
        HELP.put("find", new HelpEntry(
                "Find tasks containing a keyword (case-insensitive).",
                new String[]{
                        "find report"
                }
        ));
        HELP.put("help", new HelpEntry(
                "Show this help page. Optionally, `help <command>` for details.",
                new String[]{
                        "help",
                        "help deadline"
                }
        ));
        HELP.put("bye", new HelpEntry(
                "Ends the session (not that I’ll miss you).",
                new String[]{
                        "bye"
                }
        ));
    }

    private String allHelpText() {
        StringBuilder sb = new StringBuilder();
        sb.append("─".repeat(34)).append("\n");
        sb.append("Hmph… since you clearly need me, here’s how to use me:\n\n");
        sb.append("If you’re still lost, type help command for details on just one command.\n"  
                        + "B-but don’t expect me to explain everything forever!.\n");
        sb.append("─".repeat(34)).append("\n\n");

        for (Map.Entry<String, HelpEntry> e : HELP.entrySet()) {
            String cmd = e.getKey();
            HelpEntry entry = e.getValue();
            sb.append("• ").append(cmd).append(" — ").append(entry.description).append("\n\n");
        }
        sb.append("\nExamples:\n");
        sb.append("  - help find\n");
        sb.append("  - help deadline\n");
        return sb.toString();
    }

    private String oneTopicHelp(String topic) {
        HelpEntry entry = HELP.get(topic);
        if (entry == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("─".repeat(35)).append("\n");
        sb.append("Help: ").append(topic).append("\n");
        sb.append("─".repeat(35)).append("\n");
        sb.append(entry.description).append("\n\n");
        sb.append("Examples:\n");
        for (String ex : entry.examples) {
            sb.append("  ").append(ex).append("\n");
        }
        return sb.toString();
    }

    private static class HelpEntry {
        final String description;
        final String[] examples;

        HelpEntry(String description, String[] examples) {
            this.description = description;
            this.examples = examples;
        }
    }
}
