package jason.parser;

import jason.model.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskListTest {

    @Test
    void add_mark_unmark_delete_flow() {
        TaskList list = new TaskList();

        // add
        list.add(new Todo("buy milk"));
        assertEquals(1, list.size());
        assertEquals(false, list.get(0).isDone());

        // mark
        list.mark(0);
        assertEquals(true, list.get(0).isDone());

        // unmark
        list.unmark(0);
        assertEquals(false, list.get(0).isDone());

        // delete
        list.removeAt(0);
        assertEquals(0, list.size());
    }

    @Test
    void deadline_and_event_time_fields_are_preserved() {
        Deadline d = new Deadline("return book",
                LocalDateTime.of(2025, 9, 1, 18, 0));
        Event e = new Event("team meeting",
                LocalDateTime.of(2025, 9, 2, 14, 0),
                LocalDateTime.of(2025, 9, 2, 16, 0));

        TaskList list = new TaskList();
        list.add(d);
        list.add(e);

        // size and types
        assertEquals(2, list.size());
        assertEquals(Deadline.class, list.get(0).getClass());
        assertEquals(Event.class, list.get(1).getClass());

        // date-time fields
        assertEquals(LocalDateTime.of(2025, 9, 1, 18, 0), ((Deadline) list.get(0)).getTime());
        assertEquals(LocalDateTime.of(2025, 9, 2, 14, 0), ((Event) list.get(1)).getFrom());
        assertEquals(LocalDateTime.of(2025, 9, 2, 16, 0), ((Event) list.get(1)).getTo());
    }

    @Test
    void mark_unmark_only_affect_target_item() {
        TaskList list = new TaskList();
        list.add(new Todo("a"));
        list.add(new Todo("b"));

        list.mark(1);
        assertEquals(false, list.get(0).isDone());
        assertEquals(true, list.get(1).isDone());

        list.unmark(1);
        assertEquals(false, list.get(0).isDone());
        assertEquals(false, list.get(1).isDone());
    }

    @Test
    void operations_on_invalid_index_throw_something() {
        TaskList list = new TaskList();

        boolean threw;

        // empty list mark/unmark/delete
        threw = false;
        try {
            list.mark(0);
        } catch (Throwable t) {
            threw = true;
        }
        assertEquals(true, threw);

        threw = false;
        try {
            list.unmark(0);
        } catch (Throwable t) {
            threw = true;
        }
        assertEquals(true, threw);

        threw = false;
        try {
            list.removeAt(0);
        } catch (Throwable t) {
            threw = true;
        }
        assertEquals(true, threw);

        // out of range / negative
        list.add(new Todo("x"));
        threw = false;
        try {
            list.mark(5);
        } catch (Throwable t) {
            threw = true;
        }
        assertEquals(true, threw);

        threw = false;
        try {
            list.removeAt(-1);
        } catch (Throwable t) {
            threw = true;
        }
        assertEquals(true, threw);
    }
}
