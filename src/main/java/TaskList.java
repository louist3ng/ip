import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public TaskList(List<Task> initial) {
        if (initial != null) {
            tasks.addAll(initial);
        }
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task removeAt(int idx) {
        return tasks.remove(idx);
    }

    public void mark(int idx) {
        tasks.get(idx).mark();
    }

    public void unmark(int idx) {
        tasks.get(idx).unmark();
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public ArrayList<Task> asArrayList() {
        return new ArrayList<>(tasks);
    }

    public List<Task> find(Predicate<Task> p) {
        return tasks.stream().filter(p).toList();
    }
}
