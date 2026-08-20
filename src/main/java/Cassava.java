import task.DeadlineTask;
import task.EventTask;
import task.Task;
import task.ToDoTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cassava {
    private static String name = "Cassava";

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
        String command = scanner.nextLine();
        return command;
    }

    public static boolean handleCommand(String command) {
        printBorder();

        if (command.equals("")) {
            return handleInvalid("You have not entered any text.");
        }

        String[] args = command.split("\s");

        if (args[0].equals("list")) {
            return handleList();
        } else if (args[0].equals("bye")) {
            return handleExit();
        } else if (args[0].equals("mark")) {
            if (args.length <= 1) {
                return handleInvalid("You did not specify a task to mark.");
            }
            return handleMark(args[1]);
        } else if (args[0].equals("unmark")) {
            if (args.length <= 1) {
                return handleInvalid("You did not specify a task to unmark.");
            }
            return handleUnmark(args[1]);
        } else if (args[0].equals("todo")) {
            String[] rest = Arrays.copyOfRange(args, 1, args.length);
            String desc = String.join(" ", rest);
            return handleAddTodo(desc);
        } else if (args[0].equals("deadline")) {
            int byIndex = Arrays.asList(args).indexOf("\\by");
            if (byIndex == -1) {
                return handleInvalid("You did not specify a date for the deadline.");
            }
            String[] descArgs = Arrays.copyOfRange(args, 1, byIndex);
            String desc = String.join(" ", descArgs);
            String[] byArgs = Arrays.copyOfRange(args, byIndex + 1, args.length);
            String byDate = String.join(" ", byArgs);
            return handleAddDeadline(desc, byDate);
        } else if (args[0].equals("event")) {
            int fromIndex = Arrays.asList(args).indexOf("\\from");
            if (fromIndex == -1) {
                return handleInvalid("You did not specify a time for 'from'");
            }
            int toIndex = Arrays.asList(args).indexOf("\\to");
            if (toIndex == -1) {
                return handleInvalid("You did not specify a time for 'to'");
            }
            String[] descArgs = Arrays.copyOfRange(args, 1, fromIndex);
            String desc = String.join(" ", descArgs);
            String[] fromArgs = Arrays.copyOfRange(args, fromIndex + 1, args.length);
            String fromTime = String.join(" ", fromArgs);
            String[] toArgs = Arrays.copyOfRange(args, toIndex + 1, args.length);
            String toTime = String.join(" ", toArgs);
            return handleAddEvent(desc, fromTime, toTime);
        } else {
            return handleInvalid("Sorry, I don't recognise this command.");
        }
    }

    public static boolean handleAddTodo(String description) {
        Task task = new ToDoTask(description);
        tasks.add(task);
        System.out.println("Added: " + description);
        return false;
    }

    public static boolean handleAddDeadline(String description, String byDate) {
        Task task = new DeadlineTask(description, byDate);
        tasks.add(task);
        System.out.println("Added task: " + task.toString());
        return false;
    }

    public static boolean handleAddEvent(String description, String fromTime, String toTime) {
        Task task = new EventTask(description, fromTime, toTime);
        tasks.add(task);
        System.out.println("Added task: " + task.toString());
        return false;
    }

    public static boolean handleList() {
        if (tasks.size() == 0) {
            System.out.println("No tasks have been added yet...");
        }

        for (int i = 0; i < tasks.size(); ++i) {
            System.out.println((i+1) + ". " + tasks.get(i).toString());
        }

        return false;
    }

    public static boolean handleMark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > tasks.size()) {
                return handleInvalid("There is no such task, I cannot mark it.");
            }
            tasks.get(index).mark();

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to mark.");
        }
    }

    public static boolean handleUnmark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > tasks.size()) {
                return handleInvalid("There is no such task, I cannot unmark it.");
            }
            tasks.get(index).unmark();

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to mark.");
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
            String command = scanUserInput();
            exit = handleCommand(command);
        }
    }
}
