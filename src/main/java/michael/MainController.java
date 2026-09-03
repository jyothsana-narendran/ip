package michael;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/** Handles input and conversation updates for the FXML-defined interface. */
public class MainController {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    private final Michael michael = new Michael("data/michael.txt");
    private Image michaelPicture;
    private Image userPicture;

    @FXML
    private void initialize() {
        michaelPicture = loadAvatar("michael.png");
        userPicture = loadAvatar("user.png");
        dialogContainer.setPadding(new Insets(12, 8, 12, 8));
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        addMichaelMessage("Hi! I'm Michael. How may I help you?");
    }

    /** Processes text submitted by Enter or the Send button. */
    @FXML
    private void handleUserInput() {
        String command = userInput.getText().trim();
        if (command.isEmpty()) return;
        dialogContainer.getChildren().add(DialogBox.getUserDialog(command, userPicture));
        userInput.clear();
        addMichaelMessage(michael.getResponse(command));
    }

    private void addMichaelMessage(String message) {
        dialogContainer.getChildren().add(DialogBox.getMichaelDialog(message, michaelPicture));
    }

    private Image loadAvatar(String fileName) {
        return new Image(getClass().getResourceAsStream("/" + fileName));
    }
}
