package cassava.gui;

import static cassava.Handlers.handleInput;
import static cassava.ui.Parser.VALID_CMDS;
import static cassava.ui.Parser.parseUserInput;

import java.util.HashMap;
import java.util.List;

import cassava.Handlers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import cassava.task.Task;

public class MainWindow {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private ToggleButton darkModeToggle;

    private List<Task> tasks;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @FXML
    private void handleUserInput() {
        // Add User Dialog
        String input = userInput.getText();
        addUserDialog(input);
        userInput.clear();

        // Handle User Input
        HashMap<String, String> inputArgs = parseUserInput(input);
        Handlers.HandlerResult result = handleInput(tasks, inputArgs, VALID_CMDS);

        if (result.isError()) {
            addErrorDialog(result.message());
        } else {
            addCassavaDialog(result.message());
        }

        if (result.isExit()) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    private void handleToggleDarkMode() {
        boolean isDark = darkModeToggle.isSelected();
        if (isDark) {
            rootPane.getStyleClass().add("dark-mode");
            darkModeToggle.setText("☀");
        } else {
            rootPane.getStyleClass().remove("dark-mode");
            darkModeToggle.setText("🌙");
        }
    }

    private void addUserDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input));
    }

    public void addCassavaDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getCassavaDialog(input));
    }

    public void addErrorDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getErrorDialog(input));
    }
}
