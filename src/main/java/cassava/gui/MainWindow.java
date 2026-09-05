package cassava.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class MainWindow {
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    @FXML
    private void handleUserInput() {
        // Add User Dialog
        String input = userInput.getText();
        addUserDialog(input);
        userInput.clear();

        // Handle User Input

    }

    private void addUserDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input));
    }

    public void addCassavaDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getCassavaDialog(input));
    }
}
