package michael;

import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

/** A single chat message containing text and the sender's picture. */
public class DialogBox extends HBox {
    private final Label text;
    private final ImageView displayPicture;

    /** Creates a message bubble.
     * @param message message to display
     * @param picture image representing the sender
     * @param fromUser whether the message was sent by the user
     */
    public DialogBox(String message, Image picture, boolean fromUser) {
        text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(300);
        text.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        displayPicture = new ImageView(picture);
        displayPicture.setFitWidth(40);
        displayPicture.setFitHeight(40);
        displayPicture.setPreserveRatio(true);
        setAlignment(fromUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        setSpacing(10);
        if (fromUser) {
            getChildren().addAll(text, displayPicture);
        } else {
            getChildren().addAll(displayPicture, text);
        }
        setMaxWidth(Double.MAX_VALUE);
        getStyleClass().add(fromUser ? "user-dialog" : "michael-dialog");
    }

    /** Flips the dialog so that its image is on the right. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(children);
        getChildren().setAll(children);
    }

    /** Creates a dialog for a user message. */
    public static DialogBox getUserDialog(String message, Image picture) {
        return new DialogBox(message, picture, true);
    }

    /** Creates a flipped dialog for a Michael message. */
    public static DialogBox getMichaelDialog(String message, Image picture) {
        DialogBox dialogBox = new DialogBox(message, picture, false);
        dialogBox.flip();
        return dialogBox;
    }
}
