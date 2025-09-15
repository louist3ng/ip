package jason.storage;

import jason.model.Task;
import jason.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Storage class: loading from and saving to text files.
 */
class StorageTest {

    @TempDir
    Path tempDir;

    private Path writeLines(Path file, List<String> lines) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file, lines, StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void load_missingFile_returnsEmptyList() throws IOException {
        Path storeFile = tempDir.resolve("data").resolve("tasks.txt");
        Storage storage = new Storage(storeFile.toString());

        ArrayList<Task> tasks = storage.load();

        assertTrue(tasks.isEmpty(), "Missing file should yield an empty task list");
    }

    @Test
    void save_writesExactly_toStorageString_roundTrip() throws IOException {
        Path storeFile = tempDir.resolve("data").resolve("tasks.txt");

        // Seed file with 3 tasks we know Parser can read.
        List<String> seed = List.of(
                "T|0|Read book",
                "D|1|Submit report|2025-01-31 23:59",
                "E|0|Lecture|2025-04-01|2025-04-02"
        );
        writeLines(storeFile, seed);

        Storage storage = new Storage(storeFile.toString());

        // Load → get real Task objects produced by Parser
        ArrayList<Task> tasks = storage.load();

        // Now save them back; file content should match the tasks' toStorageString() lines
        storage.save(tasks);

        List<String> savedLines = Files.readAllLines(storeFile, StandardCharsets.UTF_8);
        List<String> expected = tasks.stream().map(Task::toStorageString).toList();

        assertEquals(expected, savedLines, 
                "Saved file must match tasks' toStorageString() output exactly");
    }

    @Test
    void save_createsParentDirectories_ifMissing() throws IOException {
        Path nested = tempDir.resolve("deep/nested/folder/tasks.txt");
        Storage storage = new Storage(nested.toString());

        // Build tasks by parsing known-good lines, so we don't rely on constructors
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(Parser.fromStorageString("T|1|CS2103T iP"));
        tasks.add(Parser.fromStorageString("D|0|Pay bill|2025-02-10"));

        storage.save(tasks);

        assertTrue(Files.exists(nested.getParent()), "Parent directories should be created");
        assertTrue(Files.exists(nested), "Target file should exist after save");

        // Verify content
        List<String> saved = Files.readAllLines(nested, StandardCharsets.UTF_8);
        List<String> expected = tasks.stream().map(Task::toStorageString).toList();
        assertEquals(expected, saved, "Saved content should match toStorageString()");
    }

    @Test
    void save_overwritesExistingFile_completely() throws IOException {
        Path storeFile = tempDir.resolve("data").resolve("tasks.txt");

        // Old content
        writeLines(storeFile, List.of(
                "T|1|OLD",
                "T|1|LINES"
        ));

        // New tasks → should replace old content
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(Parser.fromStorageString("T|0|New todo"));
        tasks.add(Parser.fromStorageString("E|1|Meetup|2025-03-01|2025-03-02"));

        Storage storage = new Storage(storeFile.toString());
        storage.save(tasks);

        List<String> saved = Files.readAllLines(storeFile, StandardCharsets.UTF_8);
        List<String> expected = tasks.stream().map(Task::toStorageString).toList();

        assertEquals(expected, saved, "Save should fully overwrite existing file with new tasks");
    }
}
