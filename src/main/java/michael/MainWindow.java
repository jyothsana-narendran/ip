package michael;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controller for the main Michael GUI. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Michael michael;
    private final Image userImage = new Image(
            getClass().getResourceAsStream("/user.png"));
    private final Image michaelImage = new Image(
            getClass().getResourceAsStream("/michael.png"));

    @FXML
    public void initialize() {
        getStylesheets().add(MainWindow.class.getResource("/view/space-theme.css").toExternalForm());
        dialogContainer.setPadding(new Insets(12, 8, 12, 8));
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        addMichaelMessage("🌌 Welcome to Mission Control — your space-themed task management app.\n\nHere, your tasks are missions: launch one with todo, deadline, or event, then track them as they orbit your task list. What mission shall we plan?");
    }

    /** Injects the Michael instance used to process commands. */
    public void setMichael(Michael michael) {
        this.michael = michael;
    }

    /** Adds the user's input and Michael's response, then clears the input field. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) return;
        String response = michael.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMichaelDialog(response, michaelImage));
        userInput.clear();
    }

    private void addMichaelMessage(String message) {
        dialogContainer.getChildren().add(
                DialogBox.getMichaelDialog(message, michaelImage));
    }
}
