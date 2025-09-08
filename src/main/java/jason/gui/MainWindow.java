package jason.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jason.command.Command;
import jason.command.CommandParser;
import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;
import jason.exception.OobIndexException;
import jason.model.Task;
import jason.model.TaskList;
import jason.storage.Storage;
import jason.ui.GuiUi;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/** Controller for MainWindow. */
public class MainWindow {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private static Image loadImage(String path) {
        InputStream is = MainWindow.class.getResourceAsStream(path);
        if (is == null) {
            throw new IllegalStateException("Image not found: " + path);
        }
        return new Image(is);
    }

    // Avatars (ensure files exist in src/main/resources/images/)
    private final Image userImage = loadImage("/images/user.png");
    private final Image jasonImage = loadImage("/images/jason.png");

    // GUI-aware Ui: pipes backend messages to the chat safely on FX thread
    private final GuiUi ui = new GuiUi(msg -> Platform.runLater(() -> appendJason(msg)));

    private final Storage storage = new Storage("data/jason.txt");
    private final TaskList tasks = new TaskList();

    @FXML
    private void initialize() {
        // Make content stretch with window
        scrollPane.setFitToWidth(true);
        dialogContainer.setFillWidth(true);

        // Auto-scroll to bottom as content grows
        dialogContainer.heightProperty().addListener((obs, o, n) -> scrollPane.setVvalue(1.0));

        // Load tasks
        try {
            List<Task> loaded = storage.load();
            loaded.forEach(tasks::add);
            if (!loaded.isEmpty()) {
                ui.showMessage("[Loaded " + loaded.size() + " tasks from disk]");
            }
        } catch (IOException e) {
            appendJason("[WARN] Could not load tasks: " + e.getMessage());
        }

        // Intro
        appendJason("Hello, my name is Jason.");
    }

    @FXML
    private void handleUserInput() {
        String text = userInput.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        dialogContainer.getChildren().add(DialogBox.user(text, userImage));
        scrollPane.setVvalue(1.0);
        userInput.clear();

        try {
            Command command = CommandParser.parse(text);
            command.execute(ui, tasks, storage); // all output routes via GuiUi -> appendJason

            if (command.isExit()) {
                appendJason("Bye. Hope to see you again soon!");
                Platform.exit();
            }
        } catch (EmptyException | OobIndexException | IncorrectInputException e) {
            appendJason(e.toString());
        } catch (NumberFormatException e) {
            appendJason("☹ OOPS!!! The task number should be an integer.");
        } catch (Exception e) {
            appendJason("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendJason(String msg) {
        dialogContainer.getChildren().add(DialogBox.jason(msg, jasonImage));
        scrollPane.setVvalue(1.0);
    }
}
