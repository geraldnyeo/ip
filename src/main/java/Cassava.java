import task.DeadlineTask;
import task.EventTask;
import task.Task;
import task.ToDoTask;

import java.util.*;

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

    private static List<Task> tasks = new ArrayList<Task>();

    public static void printIntro() {
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

    public static void printBorder() {
        System.out.println("__________________________________________________");
    }

    public static String scanUserInput() {
        Scanner scanner = new Scanner(System.in);

        printBorder();
        String input = scanner.nextLine();
        return input;
    }

    public static HashMap<String, String> parseUserInput(String input) {
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
                option.append(tokens[i]);
            }
        }
        args.put(param, option.toString());

        return args;
    }

    public static boolean handleInput(
            List<Task> tasks,
            HashMap<String, String> input_args
    ) {
        printBorder();

        String command = input_args.getOrDefault("command", "");
        String command_option = input_args.getOrDefault(command, "");
        if (command.isEmpty()) {
            return handleInvalid("You have not entered any text.");
        }
        if (!valid_cmds.contains(command)) {
            return handleInvalid("Sorry, I don't recognise this command.");
        }

        switch (command) {
            case "list":
                return handleList(tasks);
            case "todo":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to add.")
                        : handleAddTodo(tasks, command_option);
            case "deadline":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to add.")
                        : !input_args.containsKey("by")
                        ? handleInvalid("You did not specify a date for the deadline.")
                        : handleAddDeadline(tasks, command_option, input_args.get("by"));
            case "event":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to add.")
                        : !input_args.containsKey("from")
                        ? handleInvalid("You did not specify a time for 'from'.")
                        : !input_args.containsKey("to")
                        ? handleInvalid("You did not specify a time for 'to'.")
                        : handleAddEvent(tasks, command_option, input_args.get("from"), input_args.get("to"));
            case "mark":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to mark.")
                        : handleMark(tasks, command_option);
            case "unmark":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to unmark.")
                        : handleUnmark(tasks, command_option);
            case "delete":
                return command_option.isEmpty()
                        ? handleInvalid("You did not specify a task to delete.")
                        : handleDelete(tasks, command_option);
            case "bye":
                return handleExit();
            default:
                return handleInvalid("Sorry, I don't recognise this command.");
        }
    }

    public static boolean handleList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks have been added yet...");
        }

        for (int i = 0; i < tasks.size(); ++i) {
            System.out.println((i+1) + ". " + tasks.get(i).toString());
        }

        return false;
    }

    public static boolean handleAddTodo(
            List<Task> tasks,
            String description
    ) {
        Task task = new ToDoTask(description);
        tasks.add(task);
        System.out.println("Added: " + description);
        return false;
    }

    public static boolean handleAddDeadline(
            List<Task> tasks,
            String description,
            String byDate
    ) {
        Task task = new DeadlineTask(description, byDate);
        tasks.add(task);
        System.out.println("Added task: " + task.toString());
        return false;
    }

    public static boolean handleAddEvent(
            List<Task> tasks,
            String description,
            String fromTime,
            String toTime
    ) {
        Task task = new EventTask(description, fromTime, toTime);
        tasks.add(task);
        System.out.println("Added task: " + task.toString());
        return false;
    }

    public static boolean handleMark(
            List<Task> tasks,
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such task, I cannot mark it.");
            }
            tasks.get(index).mark();

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to mark.");
        }
    }

    public static boolean handleUnmark(
            List<Task> tasks,
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such task, I cannot unmark it.");
            }
            tasks.get(index).unmark();

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to mark.");
        }
    }

    public static boolean handleDelete(
            List<Task> tasks,
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such task, I cannot delete it.");
            }
            Task task = tasks.remove(index);

            System.out.println(task.toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to delete.");
        }
    }

    public static boolean handleInvalid(String msg) {
        System.out.println(msg);
        return false;
    }

    public static boolean handleExit() {
        System.out.println("Bye! See you again soon.");
        return true;
    }

    public static void main(String[] args) {
        printIntro();

        boolean exit = false;
        while (!exit) {
            String input = scanUserInput();
            HashMap<String, String> input_args = parseUserInput(input);
            exit = handleInput(tasks, input_args);
        }
    }
}
