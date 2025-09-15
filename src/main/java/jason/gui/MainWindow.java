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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

        // Ensure dialogContainer width tracks scrollPane viewport width
        scrollPane.viewportBoundsProperty().addListener((obs, oldB, newB) -> {
            dialogContainer.setPrefWidth(newB.getWidth());
        });
        // Initialize once in case listener fires later
        dialogContainer.setPrefWidth(scrollPane.getViewportBounds().getWidth());

        // Load tasks
        try {
            List<Task> loaded = storage.load();
            loaded.forEach(tasks::add);
            if (!loaded.isEmpty()) {
                ui.showMessage("I loaded " + loaded.size() 
                        + " tasks. Don't expect me to celebrate your laziness.");
            }
        } catch (IOException e) {
            appendJason("Ugh, saving failed. Fix this " + e.getMessage());
        }

        // Intro
        appendJason("Hmph… back already? Don’t get the wrong idea—I’m only here to keep you on track. " +
                "Now, what do you want me to do?");
    }

    @FXML
    private void handleUserInput() {
        String text = userInput.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        addDialog(DialogBox.user(text, userImage));
        userInput.clear();

        try {
            Command command = CommandParser.parse(text);
            command.execute(ui, tasks, storage); // all output routes via GuiUi -> appendJason

            if (command.isExit()) {
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(event -> Platform.exit());
                delay.play();
            }
        } catch (EmptyException | OobIndexException | IncorrectInputException e) {
            appendJasonError("☹ " + e.getMessage());
        } catch (NumberFormatException e) {
            appendJasonError("Tch, do you even know what a number looks like? Try again properly");
        } catch (Exception e) {
            appendJasonError(e.getMessage() == null ? "[Unexpected error]" : e.getMessage());
            e.printStackTrace();
        }
    }

    // Append a message from Jason to the dialog container
    private void appendJason(String msg) {
        addDialog(DialogBox.jason(msg, jasonImage));
    }

    // Append an error message from Jason to the dialog container
    private void appendJasonError(String msg) {
        addDialog(DialogBox.error(msg, jasonImage));
    }

    /** Ensures each DialogBox row stretches/reflows with the container width. */
    private void addDialog(DialogBox db) {
        db.prefWidthProperty().bind(dialogContainer.widthProperty());
        dialogContainer.getChildren().add(db);
        scrollPane.setVvalue(1.0);
    }
}
