import data.FileFormatException;
import task.Task;
import ui.Parser;
import ui.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static data.TaskData.getTasks;
import static data.TaskData.putTasks;

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
            HashMap<String, String> input_args = Parser.parseUserInput(input);

            UI.printBorder();
            exit = Handlers.handleInput(tasks, input_args, Parser.valid_cmds);
        }

        saveTasks();
    }
}
