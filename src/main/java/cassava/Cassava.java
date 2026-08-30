package cassava;

import static cassava.data.TaskData.getTasks;
import static cassava.data.TaskData.putTasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cassava.data.FileFormatException;
import cassava.task.Task;
import cassava.ui.Parser;
import cassava.ui.UI;

/**
 * Prompts the user to add, update or delete tasks.
 * Tasks can be marked as completed.
 * Tasks are saved in a text file after each update, and when the program exits.
 */
public class Cassava {
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

    public static void main(String[] args) {
        loadTasks();

        UI.printIntro(name);

        boolean exit = false;
        while (!exit) {
            String input = UI.scanUserInput();
            HashMap<String, String> inputArgs = Parser.parseUserInput(input);

            UI.printBorder();
            exit = Handlers.handleInput(tasks, inputArgs, Parser.VALID_CMDS);
        }

        saveTasks();
    }
}
