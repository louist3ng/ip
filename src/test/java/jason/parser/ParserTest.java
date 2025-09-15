package jason.parser;

import jason.exception.ParseException;
import jason.model.Deadline;
import jason.model.Event;
import jason.model.Task;
import jason.model.Todo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserTest {

    /* ---------------- Helpers: avoid depending on toString() ---------------- */

    /** Try to read done-state via common APIs; fall back to a best-effort toString() probe. */
    private static boolean taskIsDone(Task t) {
        try {
            // Preferred: boolean isDone()
            Method m = t.getClass().getMethod("isDone");
            Object v = m.invoke(t);
            if (v instanceof Boolean) { 
                return (Boolean) v;
            }
        } catch (ReflectiveOperationException ignored) {
            // ignore and try next approach
        }

        try {
            // Duke-style: String getStatusIcon() returns "X" or " "
            Method m = t.getClass().getMethod("getStatusIcon");
            Object v = m.invoke(t);
            if (v instanceof String) {
                String s = ((String) v).trim();
                if ("X".equals(s)) {
                    return true;
                }
                if ("".equals(s)) {
                    return false;
                }
            }
        } catch (ReflectiveOperationException ignored) {
            // ignore and try next approach
        }

        // Last resort: look for common markers in toString()
        String s = String.valueOf(t);
        if (s.contains("[X]")) {
            return true;
        }
        if (s.contains("[ ]")) {
            return false;
        }
        // If your format is different, default to "not done" to keep the test deterministic.
        return false;
    }

    private static String getDescription(Task t) {
        try {
            Method m = t.getClass().getMethod("getDescription");
            Object v = m.invoke(t);
            if (v != null) {
                return v.toString();
            }
        } catch (ReflectiveOperationException ignored) {
            // ignore and fall back to toString()
        }
        return String.valueOf(t);
    }

    /* ---------------- Happy paths ---------------- */

    @Test
    void fromStorageString_todo_done_parses() {
        Task t = Parser.fromStorageString("T|1|Buy milk");
        assertTrue(t instanceof Todo, "Should parse into a Todo");
        assertTrue(taskIsDone(t), "Todo should be marked done");
        assertTrue(getDescription(t).toLowerCase().contains("buy milk"), 
                "Description should be present");
    }

    @Test
    void fromStorageString_todo_undone_parses() {
        Task t = Parser.fromStorageString("T|0|Read book");
        assertTrue(t instanceof Todo, "Should parse into a Todo");
        assertTrue(!taskIsDone(t), "Todo should be unmarked");
        assertTrue(getDescription(t).toLowerCase().contains("read book"), 
                "Description should be present");
    }

    @Test
    void fromStorageString_deadline_isoDateTime_parses() {
        // Matches your DateTimeUtil message: yyyy-MM-dd HH:mm
        Task t = Parser.fromStorageString("D|1|Submit report|2025-01-31 23:59");
        assertTrue(t instanceof Deadline, "Should parse into a Deadline");
        assertTrue(taskIsDone(t), "Deadline should be marked done");
        // Just ensure the description came through
        assertTrue(getDescription(t).toLowerCase().contains("submit report"));
    }

    @Test
    void fromStorageString_deadline_isoDate_only_parses() {
        Task t = Parser.fromStorageString("D|0|Pay bill|2025-02-10");
        assertTrue(t instanceof Deadline, "Should parse into a Deadline");
        assertTrue(!taskIsDone(t), "Deadline should be unmarked");
        assertTrue(getDescription(t).toLowerCase().contains("pay bill"));
    }

    @Test
    void fromStorageString_event_parses() {
        // Use date-only for both endpoints to avoid formatter edge cases.
        Task t = Parser.fromStorageString("E|0|Meeting|2025-03-01|2025-03-01");
        assertTrue(t instanceof Event, "Should parse into an Event");
        assertTrue(!taskIsDone(t), "Event should be unmarked");
        assertTrue(getDescription(t).toLowerCase().contains("meeting"));
    }

    /* ---------------- Error cases ---------------- */

    @Test
    void fromStorageString_tooShort_throws() {
        assertThrows(ParseException.class, () -> Parser.fromStorageString("T|1"),
                "Lines with fewer than 3 fields should throw");
    }

    @Test
    void fromStorageString_unknownType_throws() {
        assertThrows(ParseException.class, () -> Parser.fromStorageString("X|0|Something"),
                "Unknown type should throw");
    }

    @Test
    void fromStorageString_deadline_missingDate_throws() {
        assertThrows(ParseException.class, () -> Parser.fromStorageString("D|0|No date here"),
                "Deadline without date field should throw");
    }

    @Test
    void fromStorageString_event_missingFields_throws() {
        assertAll(
                () -> assertThrows(ParseException.class,
                        () -> Parser.fromStorageString("E|1|Party"),
                        "Event with no from/to should throw"),
                () -> assertThrows(ParseException.class,
                        () -> Parser.fromStorageString("E|1|Party|2025-01-01"),
                        "Event missing 'to' should throw")
        );
    }

    /* ---------------- Done flag check ---------------- */

    @Test
    void fromStorageString_event_done_flag_respected() {
        Task finish = Parser.fromStorageString("E|1|Lecture|2025-04-01|2025-04-01");
        Task unfinished = Parser.fromStorageString("E|0|Lecture|2025-04-01|2025-04-01");
        assertTrue(taskIsDone(finish), "Done Event should be detected as done");
        assertTrue(!taskIsDone(unfinished), "Undone Event should be detected as not done");
    }

    /* ---------------- Whitespace handling ---------------- */

    @Test
    void fromStorageString_whitespaceAroundPipes_isIgnoredForTypeAndDone() {
        Task t = Parser.fromStorageString("   T   |   1   |   Trim   test   ");
        assertTrue(t instanceof Todo, "Should parse into a Todo");
        assertTrue(taskIsDone(t), "Whitespace should not affect done flag");
        String desc = getDescription(t).toLowerCase().replaceAll("\\s+", " ").trim();
        assertTrue(desc.contains("trim test"), "Description should be preserved");
    }
}
