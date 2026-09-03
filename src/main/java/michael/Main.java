package michael;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX entry point for Michael's chat-style user interface. */
public class Main extends Application {
    private final Michael michael = new Michael("data/michael.txt");
    private Image michaelPicture;
    private Image userPicture;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;

    @Override
    public void start(Stage stage) {
        michaelPicture = loadAvatar("michael.png");
        userPicture = loadAvatar("user.png");
        scrollPane = new ScrollPane();
        dialogContainer = new VBox(10);
        dialogContainer.setPadding(new Insets(12, 8, 12, 8));
        dialogContainer.setFillWidth(true);
        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);

        userInput = new TextField();
        userInput.setPromptText("Enter a command, e.g. todo read a book");
        Button sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane(scrollPane, userInput, sendButton);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 50.0);
        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(userInput, 85.0);
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);
        AnchorPane.setBottomAnchor(sendButton, 10.0);

        stage.setTitle("Michael");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);
        mainLayout.setPrefSize(400.0, 600.0);
        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        userInput.setPrefWidth(325.0);
        sendButton.setPrefWidth(55.0);
        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setLeftAnchor(scrollPane, 1.0);
        AnchorPane.setRightAnchor(scrollPane, 15.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setRightAnchor(userInput, 56.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        Scene scene = new Scene(mainLayout, 400, 600);
        stage.setScene(scene);
        stage.show();
        addMichaelMessage("Hi! I'm Michael. How may I help you?");

        // Handle both clicking Send and pressing Enter in the input field.
        sendButton.setOnMouseClicked(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());

        // Keep the newest message visible whenever the conversation grows.
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Creates a user dialog, processes the command, and displays Michael's reply.
     * The input field is cleared after the command is captured.
     */
    private void handleUserInput() {
        String command = userInput.getText().trim();
        if (command.isEmpty()) return;
        addUserMessage(command);
        userInput.clear();
        String response = michael.processCommand(command);
        addMichaelMessage(response);
    }

    private void addUserMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, userPicture, true));
    }

    private void addMichaelMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox(message, michaelPicture, false));
        scrollPane.setVvalue(1.0);
    }

    /** Loads an avatar from the application's bundled image resources. */
    private Image loadAvatar(String fileName) {
        return new Image(getClass().getResourceAsStream("/" + fileName));
    }
}
