package jason.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
    }

    /**
     * Constructs a TaskList with the given initial tasks.
     *
     * @param initial The initial tasks to add to the list.
     */
    public TaskList(List<Task> initial) {
        if (initial != null) {
            tasks.addAll(initial);
        }
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to add.
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param idx The index of the task to remove.
     * @return The removed task, or null if the index is invalid.
     */
    public Task removeAt(int idx) {
        return tasks.remove(idx);
    }

    /**
     * Marks a task as done at the specified index.
     *
     * @param idx The index of the task to mark.
     */
    public void mark(int idx) {
        tasks.get(idx).mark();
    }

    /**
     * Marks a task as not done at the specified index.
     *
     * @param idx The index of the task to unmark.
     */
    public void unmark(int idx) {
        tasks.get(idx).unmark();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param idx The index of the task to retrieve.
     * @return The task at the specified index, or null if the index is invalid.
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Returns a list of tasks as an ArrayList.
     *
     * @return A list of tasks as an ArrayList.
     */
    public ArrayList<Task> asArrayList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds tasks in the list that match the given predicate.
     *
     * @param p The predicate to match tasks against.
     * @return A list of tasks that match the predicate.
     */
    public List<Task> find(Predicate<Task> p) {
        return tasks.stream().filter(p).toList();
    }
}
