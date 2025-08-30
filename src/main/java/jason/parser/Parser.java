package jason.parser;

import jason.exception.ParseException;
import jason.model.Deadline;
import jason.model.Event;
import jason.model.Task;
import jason.model.Todo;
import java.time.LocalDateTime;

/**
 * Parser class to handle user input and storage string parsing.
 */
public class Parser {
    /**
     * Extracts the command from a user input string.
     * @param s the input string
     * @return the extracted command
     */
    public static String taskCommand(String s) {
        String trimmed = s.trim();
        int sp = trimmed.indexOf(' ');
        return (sp == -1 ? trimmed : trimmed.substring(0, sp)).toLowerCase();
    }

    /**
     * Extracts the content from a user input string.
     * @param s the input string
     * @return the extracted content
     */
    public static String taskContent(String s) {
        String trimmed = s.trim();
        int sp = trimmed.indexOf(' ');
        return (sp == -1 ? "" : trimmed.substring(sp + 1).trim());
    }

    /**
     * Parses a task from a storage string.
     * @param line the storage string
     * @return the parsed Task
     */
    public static Task fromStorageString(String line) {
        String[] p = line.split("\\|");
        if (p.length < 3) {
            throw new ParseException("Corrupt line: " + line);
        }

        String type = p[0].trim();
        boolean done = "1".equals(p[1].trim());
        String desc = p[2];

        switch (type) {
            case "T" -> {
                Todo t = new Todo(desc);
                if (done) {
                    t.mark();
                } else {
                    t.unmark();
                }
                return t;
            }
            case "D" -> {
                if (p.length < 4) {
                    throw new ParseException("Bad D line: " + line);
                }
                LocalDateTime by = DateTimeUtil.parseIsoDateOrDateTime(p[3]);
                var d = new Deadline(desc, by);
                if (done) {
                    d.mark();
                } else {
                    d.unmark();
                }
                return d;
            }
            case "E" -> {
                if (p.length < 5) {
                    throw new ParseException("Bad E line: " + line);
                }
                LocalDateTime from = DateTimeUtil.parseIsoDateOrDateTime(p[3]);
                LocalDateTime to = DateTimeUtil.parseIsoDateOrDateTime(p[4]);
                var e = new Event(desc, from, to);
                if (done) {
                    e.mark();
                } else {
                    e.unmark();
                }
                return e;
            }
            default -> throw new ParseException("Unknown type: " + type);
        }
    }
}
