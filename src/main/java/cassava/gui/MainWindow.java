package cassava.gui;

import static cassava.Handlers.handleInput;
import static cassava.ui.Parser.VALID_CMDS;
import static cassava.ui.Parser.parseUserInput;

import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import cassava.task.Task;

public class MainWindow {
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;

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
        boolean exit = handleInput(this, tasks, inputArgs, VALID_CMDS);

        if (exit) {
            Platform.exit();
            System.exit(0);
        }
    }

    private void addUserDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input));
    }

    public void addCassavaDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getCassavaDialog(input));
    }
}
