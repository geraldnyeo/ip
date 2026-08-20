import task.DeadlineTask;
import task.Task;
import task.ToDoTask;

import java.util.Arrays;
import java.util.Scanner;

public class Cassava {
    private static String name = "Cassava";

    private static Task[] tasks = new Task[100];
    private static int numTasks = 0;

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
        } else {
            return handleInvalid("Sorry, I don't recognise this command.");
        }
    }

    public static boolean handleAddTodo(String description) {
        if (numTasks < 100) {
            Task task = new ToDoTask(description);
            tasks[numTasks++] = task;
            System.out.println("Added: " + description);
        } else {
            System.out.println("Sorry, there's no more space to add further tasks...");
        }

        return false;
    }

    public static boolean handleAddDeadline(String description, String byDate) {
        if (numTasks < 100) {
            Task task = new DeadlineTask(description, byDate);
            tasks[numTasks++] = task;
            System.out.println("Added task: " + task.toString());
        } else {
            System.out.println("Sorry, there's no more space to add further tasks...");
        }

        return false;
    }

    public static boolean handleList() {
        if (numTasks == 0) {
            System.out.println("No tasks have been added yet...");
        }

        for (int i = 0; i < numTasks; ++i) {
            System.out.println((i+1) + ". " + tasks[i].toString());
        }

        return false;
    }

    public static boolean handleMark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > numTasks) {
                return handleInvalid("There is no such task, I cannot mark it.");
            }
            tasks[index].mark();

            System.out.println(tasks[index].toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the task you wish to mark.");
        }
    }

    public static boolean handleUnmark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > numTasks) {
                return handleInvalid("There is no such task, I cannot unmark it.");
            }
            tasks[index].unmark();

            System.out.println(tasks[index].toString());

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
