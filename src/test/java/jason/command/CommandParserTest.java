package jason.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;

/**
 * Tests for {@link CommandParser} using only assertEquals.
 */
class CommandParserTest {

    @Test
    void parse_bye_ok() {
        Command c = CommandParser.parse("bye");
        assertEquals(ByeCommand.class, c.getClass());
    }

    @Test
    void parse_bye_withExtraText_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("bye now"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }

    @Test
    void parse_list_ok() {
        Command c = CommandParser.parse("list");
        assertEquals(ListCommand.class, c.getClass());
    }

    @Test
    void parse_list_withExtraText_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("list something"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }

    @Test
    void parse_todo_ok() {
        Command c = CommandParser.parse("todo read book");
        assertEquals(TodoCommand.class, c.getClass());
    }

    @Test
    void parse_find_ok() {
        Command c = CommandParser.parse("find keyword");
        assertEquals(FindCommand.class, c.getClass());
    }

    @Test
    void parse_find_empty_throws() {
        Exception e = assertThrows(EmptyException.class,
                () -> CommandParser.parse("find   "));
        assertEquals(EmptyException.class, e.getClass());
    }

    @Test
    void parse_deadline_ok() {
        Command c = CommandParser.parse("deadline return book /by 2025-09-05");
        assertEquals(DeadlineCommand.class, c.getClass());
    }

    @Test
    void parse_deadline_missingBy_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("deadline return book"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }

    @Test
    void parse_event_ok() {
        Command c = CommandParser.parse("event meeting /from 10am /to 12pm");
        assertEquals(EventCommand.class, c.getClass());
    }

    @Test
    void parse_event_missingMarkers_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("event meeting tomorrow"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }

    @Test
    void parse_mark_ok() {
        Command c = CommandParser.parse("mark 1");
        assertEquals(MarkCommand.class, c.getClass());
    }

    @Test
    void parse_mark_invalidIndex_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("mark x"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }

    @Test
    void parse_mark_emptyIndex_throws() {
        Exception e = assertThrows(EmptyException.class,
                () -> CommandParser.parse("mark   "));
        assertEquals(EmptyException.class, e.getClass());
    }

    @Test
    void parse_unmark_ok() {
        Command c = CommandParser.parse("unmark 2");
        assertEquals(UnmarkCommand.class, c.getClass());
    }

    @Test
    void parse_delete_ok() {
        Command c = CommandParser.parse("delete 3");
        assertEquals(DeleteCommand.class, c.getClass());
    }

    @Test
    void parse_whitespace_caseInsensitive() {
        Command c1 = CommandParser.parse("   LIST   ");
        assertEquals(ListCommand.class, c1.getClass());

        Command c2 = CommandParser.parse("Bye");
        assertEquals(ByeCommand.class, c2.getClass());

        Command c3 = CommandParser.parse("ToDo read");
        assertEquals(TodoCommand.class, c3.getClass());
    }

    @Test
    void parse_invalidCommand_throws() {
        Exception e = assertThrows(IncorrectInputException.class,
                () -> CommandParser.parse("unknown"));
        assertEquals(IncorrectInputException.class, e.getClass());
    }
}
