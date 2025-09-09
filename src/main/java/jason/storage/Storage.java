package jason.storage;

import jason.model.Task;
import jason.parser.Parser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Storage class to handle loading and saving tasks to disk.
 */
public class Storage {
    private final Path file;

    /**
     * Constructor for Storage.
     * @param relativePath the relative path to the storage file
     */
    public Storage(String relativePath) {
        this.file = Paths.get(relativePath);
        assert relativePath != null; // caller should ensure non-null
    }

    /**
     * Ensures that the parent directory for the storage file exists.
     * @throws IOException if an I/O error occurs
     */
    private void ensureDir() throws IOException {
        assert file != null; // never null
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    /**
     * Loads tasks from disk. If file missing, returns empty list.
     * @return the list of tasks
     * @throws IOException if an I/O error occurs
     */
    public ArrayList<Task> load() throws IOException {
        ensureDir();
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks; // empty if file not found
        }
        for (String line : Files.readAllLines(file)) {
            if (!line.trim().isEmpty()) {
                tasks.add(Parser.fromStorageString(line));
            }
        }
        return tasks;
    }

    /**
     * Saves tasks atomically (write to temp then move).
     * @param tasks the list of tasks to save
     * @throws IOException if an I/O error occurs
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        assert tasks != null; // caller should ensure non-null
        ensureDir();
        Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
        assert !tmp.equals(file); // temp file should be different
        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (Task t : tasks) {
                assert t != null; // tasks list should not contain null
                bw.write(t.toStorageString());
                bw.newLine();
            }
        }
        Files.move(tmp, file,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
}
