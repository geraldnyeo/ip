package cassava;

import static cassava.data.TaskData.getTasks;
import static cassava.data.TaskData.putTasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import cassava.data.FileFormatException;
import cassava.task.Task;

/**
 * Prompts the user to add, update or delete tasks.
 * Tasks can be marked as completed.
 * Tasks are saved in a text file after each update, and when the program exits.
 */
public class Cassava extends Application {
    private static final String name = "Cassava";

    private static final List<Task> tasks = new ArrayList<Task>();

    private static void loadTasks() {
        List<Task> savedTasks = new ArrayList<>();

        try {
            savedTasks = getTasks();
        } catch (IOException | FileFormatException e) {
            System.out.println(e);
            System.exit(1);
        }

        tasks.addAll(savedTasks);
    }

    private static void saveTasks() {
        try {
            putTasks(tasks);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Cassava.class.getResource("/views/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}
