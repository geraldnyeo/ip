import data.FileFormatException;
import task.Task;

import java.io.IOException;
import java.util.*;

import static data.TaskData.getTasks;
import static data.TaskData.putTasks;

public class Cassava {
    private static final List<String> valid_cmds = new ArrayList<String>(
            Arrays.asList(
                "list",
                "todo",
                "deadline",
                "event",
                "mark",
                "unmark",
                "delete",
                "bye"
            )
    );

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

    private static void printIntro() {
        String banner = "  ____                               \n" +
                " / ___|__ _ ___ ___  __ ___   ____ _\n" +
                "| |   / _` / __/ __|/ _` \\ \\ / / _` |\n" +
                "| |__| (_| \\__ \\__ \\ (_| |\\ V / (_| |\n" +
                " \\____\\__,_|___/___/\\__,_| \\_/ \\__,_|";
        System.out.println(banner);

        System.out.println("__________________________________________________");
        System.out.println("Hi! I'm " + name + ".");
        System.out.println("How can I help you today?");
    }

    private static void printBorder() {
        System.out.println("__________________________________________________");
    }

    private static String scanUserInput() {
        Scanner scanner = new Scanner(System.in);

        printBorder();
        String input = scanner.nextLine();
        return input;
    }

    private static HashMap<String, String> parseUserInput(String input) {
        HashMap<String, String> args = new HashMap<String, String>();
        String[] tokens = input.split("\s");

        args.put("command", tokens[0]);

        String param = tokens[0];
        StringBuilder option = new StringBuilder();
        for (int i = 1; i < tokens.length; ++i) {
            if (tokens[i].charAt(0) == '\\') {
                args.put(param, option.toString());
                param = tokens[i].substring(1);
                option = new StringBuilder();
            } else {
                option.append(option.isEmpty() ? tokens[i] : " " + tokens[i]);
            }
        }
        args.put(param, option.toString());

        return args;
    }

    public static void main(String[] args) {
        loadTasks();

        printIntro();

        boolean exit = false;
        while (!exit) {
            String input = scanUserInput();
            HashMap<String, String> input_args = parseUserInput(input);

            printBorder();
            exit = Handlers.handleInput(tasks, input_args, valid_cmds);
        }

        saveTasks();
    }
}
