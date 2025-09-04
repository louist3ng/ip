package jason.parser;

import jason.exception.ParseException;
import jason.model.Deadline;
import jason.model.Event;
import jason.model.Task;
import jason.model.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserFromStorageTest {

    /* -------------------- Todo -------------------- */

    @Test
    void parse_todo_unmarked_ok() {
        Task t = Parser.fromStorageString("T | 0 | read book");
        assertEquals(Todo.class, t.getClass());
        // assuming Task has isDone() and getDescription()
        assertEquals(false, t.isDone());
        assertEquals("read book", t.getDescription());
    }

    @Test
    void parse_todo_marked_ok() {
        Task t = Parser.fromStorageString("T | 1 | groceries");
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
    void parse_event_ok() {
        Task t = Parser.fromStorageString("E | 0 | project | 2025-09-10 10:00 | 2025-09-10 12:00");
        assertEquals(Event.class, t.getClass());
        assertEquals(false, t.isDone());
        assertEquals("project", t.getDescription());
        assertEquals(LocalDateTime.of(2025, 9, 10, 10, 0), ((Event) t).getFrom());
        assertEquals(LocalDateTime.of(2025, 9, 10, 12, 0), ((Event) t).getTo());
    }

    @Test
    void parse_event_missingField_throwsParseException() {
        Exception e = assertThrows(ParseException.class,
                () -> Parser.fromStorageString("E | 1 | meeting | 2025-09-10 10:00"));
        assertEquals(ParseException.class, e.getClass());
    }

    @Test
    void parse_event_badDate_throwsDateTimeParseException() {
        Exception e = assertThrows(DateTimeParseException.class,
                () -> Parser.fromStorageString("E | 1 | mtg | 10-09-2025 10:00 | 2025-09-10 12:00"));
        assertEquals(DateTimeParseException.class, e.getClass());
    }

    /* -------------------- Generic error paths -------------------- */

    @Test
    void parse_corruptLine_tooFewFields_throwsParseException() {
        Exception e = assertThrows(ParseException.class,
                () -> Parser.fromStorageString("X | 1"));
        assertEquals(ParseException.class, e.getClass());
    }

    @Test
    void parse_unknownType_throwsParseException() {
        Exception e = assertThrows(ParseException.class,
                () -> Parser.fromStorageString("Z | 1 | something"));
        assertEquals(ParseException.class, e.getClass());
    }
}
