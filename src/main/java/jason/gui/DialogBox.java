package jason.gui;

// no javax.script.Bindings import!
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 * A dialog box consisting of an ImageView to represent the speaker's face and a
 * label containing text from the speaker.
 */
public class DialogBox extends HBox {
    private static final double AVATAR_SIZE = 40.0;

    // If the window (scene) is at least this wide, we consider it "fullscreen/wide"
    private static final double FULL_WIDTH_THRESHOLD = 900.0;

    // In fullscreen/wide mode, wrap bubbles to this max width (or available width
    // if smaller)
    private static final double MAX_BUBBLE_WIDTH_WIDE = 680.0;

    private final Label text = new Label();
    private final ImageView avatar = new ImageView();

    private DialogBox(String message, Image img) {
        // text bubble
        text.setText(message);
        HBox.setHgrow(text, Priority.ALWAYS);
        text.setMinWidth(0); // allow shrinking
        text.setPrefWidth(Region.USE_COMPUTED_SIZE);
        text.setMaxWidth(Double.MAX_VALUE);

        // Decide behavior based on the *window* size (scene width)
        sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene == null)
                return;

            var isWide = scene.widthProperty().greaterThanOrEqualTo(FULL_WIDTH_THRESHOLD);

            // Always allow wrapping (especially in narrow mode)
            text.setWrapText(true);

            var available = widthProperty()
                    .subtract(AVATAR_SIZE) // avatar
                    .subtract(getSpacing()) // gap
                    .subtract(24); // padding + safety

            text.maxWidthProperty().bind(
                    javafx.beans.binding.Bindings.when(isWide)
                            .then(
                                    javafx.beans.binding.Bindings.min(
                                            MAX_BUBBLE_WIDTH_WIDE, // cap when wide
                                            available))
                            .otherwise(available) // in narrow mode: use full width
            );
        });

        // circular avatar
        avatar.setImage(img);
        avatar.setFitWidth(AVATAR_SIZE);
        avatar.setFitHeight(AVATAR_SIZE);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        // circle clip; bind so it stays round if size changes (zoom, DPI scaling)
        Circle clip = new Circle(AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0);
        clip.radiusProperty().bind(avatar.fitWidthProperty().divide(2));
        clip.centerXProperty().bind(avatar.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(avatar.fitHeightProperty().divide(2));
        avatar.setClip(clip);

        // row layout
        setSpacing(10);
        setPadding(new Insets(6));
        setFillHeight(true);
        setMaxWidth(Double.MAX_VALUE); // let HBox span the row
        setAlignment(Pos.TOP_LEFT); // default: bot on left
        getChildren().addAll(avatar, text);
    }

    /** Put user messages on the right: swap children + right align. */
    private void flip() {
        Node a = getChildren().get(0); // avatar
        Node b = getChildren().get(1); // text
        getChildren().setAll(b, a);
        setAlignment(Pos.TOP_RIGHT);
        text.setAlignment(Pos.CENTER_RIGHT);
    }

    /** Jason / bot bubble (left). */
    public static DialogBox jason(String message, Image img) {
        DialogBox db = new DialogBox(message, img);
        db.text.getStyleClass().add("bubble-bot");
        return db;
    }

    /** User bubble (right). */
    public static DialogBox user(String message, Image img) {
        DialogBox db = new DialogBox(message, img);
        db.flip();
        db.text.getStyleClass().add("bubble-user");
        return db;
    }

    /** Error bubble (left, bot-side). */
    public static DialogBox error(String message, Image img) {
        DialogBox db = new DialogBox(message, img);
        db.text.getStyleClass().addAll("bubble-bot", "bubble-error");
        return db;
    }
}
