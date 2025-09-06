package jason.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import jason.exception.ParseException;
import jason.model.Deadline;
import jason.model.Task;
import jason.model.Todo;

class ParserTest {

    // ---------- fromStorageString (valid lines) ----------
    @Test
    void fromStorage_Todo_done_flag_sets_marked() {
        Task t = Parser.fromStorageString("T|1|buy milk");
        // class check using assertEquals
        assertEquals(Todo.class, t.getClass());
        assertEquals(true, t.isDone());
        assertEquals("groceries", t.getDescription());
    }

    /* -------------------- Deadline -------------------- */

    @Test
    void parse_deadline_dateOnly_ok() {
        Task t = Parser.fromStorageString("D | 1 | return book | 2025-09-05");
        assertEquals(Deadline.class, t.getClass());
        assertEquals(true, t.isDone());
        assertEquals("return book", t.getDescription());
        // assuming Deadline exposes getTime()
        LocalDateTime expected = LocalDateTime.of(2025, 9, 5, 0, 0);
        assertEquals(expected, ((Deadline) t).getTime());
    }

    @Test
    void parse_deadline_dateTime_ok() {
        Task t = Parser.fromStorageString("D | 0 | submit | 2025-09-05 23:59");
        assertEquals(Deadline.class, t.getClass());
        assertEquals(false, t.isDone());
        assertEquals("submit", t.getDescription());
        assertEquals(LocalDateTime.of(2025, 9, 5, 23, 59), ((Deadline) t).getTime());
    }

    @Test
    void parse_deadline_missingField_throwsParseException() {
        Exception e = assertThrows(ParseException.class,
                () -> Parser.fromStorageString("D | 0 | only three fields"));
        assertEquals(ParseException.class, e.getClass());
    }

    @Test
    void parse_deadline_badDate_throwsDateTimeParseException() {
        Exception e = assertThrows(DateTimeParseException.class,
                () -> Parser.fromStorageString("D | 0 | desc | 2025/09/05"));
        assertEquals(DateTimeParseException.class, e.getClass());
    }

    /* -------------------- Event -------------------- */

    @Test
    void fromStorage_corrupt_line_too_few_fields_throws() {
        boolean threw = false;
        try {
            Parser.fromStorageString("T|1");
        } catch (ParseException e) {
            threw = true;
        } catch (Throwable ignored) {
            // no-op
        }
        assertEquals(true, threw);
    }

    @Test
    void fromStorage_deadline_missing_date_throws() {
        boolean threw = false;
        try {
            Parser.fromStorageString("D|0|return book");
        }catch (ParseException e) {
            threw = true;
        } catch (Throwable ignored) {
            // no-op
        }
        assertEquals(true, threw);
    }

    @Test
    void fromStorage_event_missing_to_throws() {
        boolean threw = false;
        try {
            Parser.fromStorageString("E|1|meeting|2025-09-02 14:00");
        } catch (ParseException e) {
            threw = true;
        } catch (Throwable ignored) {
            // no-op
        }
        assertEquals(true, threw);
    }

    @Test
    void fromStorage_unknown_type_throws() {
        boolean threw = false;
        try {
            Parser.fromStorageString("X|1|something");
        } catch (ParseException e) {
            threw = true;
        } catch (Throwable ignored) {
            // no-op
        }
        assertEquals(true, threw);
    }
}
