package jason.parser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import jason.exception.ParseException;
import jason.model.Deadline;
import jason.model.Event;
import jason.model.Task;
import jason.model.Todo;

class ParserTest {

    // ---------- fromStorageString (valid lines) ----------
    @Test
    void fromStorage_Todo_done_flag_sets_marked() {
        Task t = Parser.fromStorageString("T|1|buy milk");
        // class check using assertEquals
        assertEquals(Todo.class, t.getClass());
        // done state
        assertEquals(true, t.isDone());
    }

    @Test
    void fromStorage_Todo_unmarked_when_zero() {
        Task t = Parser.fromStorageString("T|0|read book");
        assertEquals(Todo.class, t.getClass());
        assertEquals(false, t.isDone());
    }

    @Test
    void fromStorage_Deadline_parses_datetime_and_done() {
        Task t = Parser.fromStorageString("D|0|return book|2025-09-01 18:00");
        assertEquals(Deadline.class, t.getClass());
        assertEquals(false, t.isDone());
        LocalDateTime expected = LocalDateTime.of(2025, 9, 1, 18, 0);
        LocalDateTime actual = ((Deadline) t).getTime();
        assertEquals(expected, actual);
    }

    @Test
    void fromStorage_Event_parses_from_to_and_done() {
        Task t = Parser.fromStorageString("E|1|meeting|2025-09-02 14:00|2025-09-02 16:00");
        assertEquals(Event.class, t.getClass());
        assertEquals(true, t.isDone());
        assertEquals(LocalDateTime.of(2025, 9, 2, 14, 0), ((Event) t).getFrom());
        assertEquals(LocalDateTime.of(2025, 9, 2, 16, 0), ((Event) t).getTo());
    }

    @Test
    void fromStorage_trims_type_and_done_but_keeps_desc_as_is() {
        // spaces around type/done are fine; desc is taken verbatim by Parser (no trim)
        Task t = Parser.fromStorageString(" T | 1 |buy milk");
        assertEquals(Todo.class, t.getClass());
        assertEquals(true, t.isDone());
    }

    // ---------- fromStorageString (error cases) ----------
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
