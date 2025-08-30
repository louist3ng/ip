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

public class Storage {
    private final Path file;

    public Storage(String relativePath) {
        this.file = Paths.get(relativePath);
    }

    /** 
     * Ensure parent dir exists.
    */
    private void ensureDir() throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    /** Load tasks from disk. If file missing, return empty list. */
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

    /** Save tasks atomically (write to temp then move). */
    public void save(ArrayList<Task> tasks) throws IOException {
        ensureDir();
        Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (Task t : tasks) {
                bw.write(t.toStorageString());
                bw.newLine();
            }
        }
        Files.move(tmp, file,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
}
