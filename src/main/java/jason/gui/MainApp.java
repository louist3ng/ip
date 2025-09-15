package jason.gui;

import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Main application class to launch the JavaFX GUI. */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = MainApp.class.getResource("/jason/MainWindow.fxml");
        Objects.requireNonNull(fxml, "FXML not found: /jason/MainWindow.fxml");
        Scene scene = new Scene(new FXMLLoader(fxml).load());
        stage.setTitle("Jason");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        new Zoom().install(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
