package michael;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** A single chat message containing text and the sender's picture. */
public class DialogBox extends HBox {
    private final Label text;
    private final ImageView displayPicture;

    /** Creates a message bubble.
     * @param message message to display
     * @param picture image representing the sender
     */
    public DialogBox(String message, Image picture) {
        text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(520);
        displayPicture = new ImageView(picture);
        displayPicture.setFitWidth(40);
        displayPicture.setFitHeight(40);
        displayPicture.setPreserveRatio(true);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);
        getChildren().addAll(displayPicture, text);
        getStyleClass().add("dialog-box");
    }
}
