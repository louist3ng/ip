package jason.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A dialog box consisting of an ImageView to represent the speaker's face and a label
 * containing text from the speaker.
 */
public class DialogBox extends HBox {
    private final Label text = new Label();
    private final ImageView avatar = new ImageView();

    private DialogBox(String message, Image img) {
        text.setText(message);
        text.setWrapText(true);
        text.setMaxWidth(360);

        avatar.setImage(img);
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        setSpacing(10);
        setPadding(new Insets(6));
        setFillHeight(true);

        // IMPORTANT: let this HBox stretch to full row width so RIGHT alignment works
        setMaxWidth(Double.MAX_VALUE); // <-- changed from USE_PREF_SIZE
        setAlignment(Pos.TOP_LEFT); // default for bot/Jason

        getChildren().addAll(avatar, text); // add only once (left side default)
    }

    /** Put user messages on the right: swap children + right align. */
    private void flip() {
        // swap (safer than reversing live list)
        Node a = getChildren().get(0); // avatar
        Node b = getChildren().get(1); // text
        getChildren().setAll(b, a);

        // align the whole bubble to the right edge of the row
        setAlignment(Pos.TOP_RIGHT); // <-- was TOP_LEFT before
        // Optional: right-align text inside the bubble
        text.setAlignment(Pos.CENTER_RIGHT);
    }

    /**
     * Creates a dialog box for Jason's messages.
     *
     * @param message the message to display
     * @param img     the image to display
     * @return a DialogBox for Jason's messages
     */
    public static DialogBox jason(String message, Image img) {
        DialogBox db = new DialogBox(message, img);
        db.text.getStyleClass().add("bubble-bot");
        return db;
    }

    /**
     * Creates a dialog box for the user's messages.
     *
     * @param message the message to display
     * @param img     the image to display
     * @return a DialogBox for the user's messages
     */
    public static DialogBox user(String message, Image img) {
        DialogBox db = new DialogBox(message, img);
        db.flip(); // right side
        db.text.getStyleClass().add("bubble-user");
        return db;
    }
}
