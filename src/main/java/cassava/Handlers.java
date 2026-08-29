package cassava;

import static cassava.data.TaskData.putTasks;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import cassava.task.DeadlineTask;
import cassava.task.EventTask;
import cassava.task.Task;
import cassava.task.ToDoTask;

public class Handlers {

    public static boolean handleInput(
            List<Task> tasks,
            HashMap<String, String> inputArgs,
            List<String> validCmds
    ) {
        String command = inputArgs.getOrDefault("command", "");
        String commandOption = inputArgs.getOrDefault(command, "");
        if (command.isEmpty()) {
            return handleInvalid("You have not entered any text.");
        }
        if (!validCmds.contains(command)) {
            return handleInvalid("Sorry, I don't recognise this command.");
        }

        return switch (command) {
            case "list" -> handleList(tasks);
            case "todo" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : handleAddTodo(tasks, commandOption);
            case "deadline" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : !inputArgs.containsKey("by")
                    ? handleInvalid("You did not specify a date for the deadline.")
                    : handleAddDeadline(tasks, commandOption, inputArgs.get("by"));
            case "event" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : !inputArgs.containsKey("from")
                    ? handleInvalid("You did not specify a time for 'from'.")
                    : !inputArgs.containsKey("to")
                    ? handleInvalid("You did not specify a time for 'to'.")
                    : handleAddEvent(tasks, commandOption, inputArgs.get("from"), inputArgs.get("to"));
            case "mark" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to mark.")
                    : handleMark(tasks, commandOption);
            case "unmark" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to unmark.")
                    : handleUnmark(tasks, commandOption);
            case "delete" -> commandOption.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to delete.")
                    : handleDelete(tasks, commandOption);
            case "bye" -> handleExit();
            default -> handleInvalid("Sorry, I don't recognise this command.");
        };
    }

    public static boolean handleList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks have been added yet...");
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }

        return false;
    }

    public static boolean handleAddTodo(
            List<Task> tasks,
            String description
    ) {
        Task task = new ToDoTask(description);
        tasks.add(task);
        saveTasks(tasks);
        System.out.println("Added: " + description);
        return false;
    }

    public static boolean handleAddDeadline(
            List<Task> tasks,
            String description,
            String byDateString
    ) {
        LocalDate byDate = LocalDate.now();
        try {
            byDate = LocalDate.parse(byDateString);
        } catch (DateTimeParseException e) {
            return handleInvalid("You must input a date in the format 'yyyy-mm-dd' for the 'by' argument.");
        }

        Task task = new DeadlineTask(description, byDate);
        tasks.add(task);
        saveTasks(tasks);

        System.out.println("Added cassava.task: " + task.toString());

        return false;
    }

    public static boolean handleAddEvent(
            List<Task> tasks,
            String description,
            String fromDateString,
            String toDateString
    ) {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        try {
            fromDate = LocalDate.parse(fromDateString);
            toDate = LocalDate.parse(toDateString);
        } catch (DateTimeParseException e) {
            return handleInvalid("You must input a date in the format 'yyyy-mm-dd' "
                    + "for the 'from' and 'by arguments.");
        }

        Task task = new EventTask(description, fromDate, toDate);
        tasks.add(task);
        saveTasks(tasks);

        System.out.println("Added cassava.task: " + task.toString());

        return false;
    }

    public static boolean handleMark(
            List<Task> tasks,
            String indexArg
    ) {
        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such cassava.task, I cannot mark it.");
            }
            tasks.get(index).mark();
            saveTasks(tasks);

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the cassava.task you wish to mark.");
        }
    }

    public static boolean handleUnmark(
            List<Task> tasks,
            String indexArg
    ) {
        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such cassava.task, I cannot unmark it.");
            }
            tasks.get(index).unmark();
            saveTasks(tasks);

            System.out.println(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the cassava.task you wish to mark.");
        }
    }

    public static boolean handleDelete(
            List<Task> tasks,
            String indexArg
    ) {
        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid("There is no such cassava.task, I cannot delete it.");
            }
            Task task = tasks.remove(index);
            saveTasks(tasks);

            System.out.println(task.toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the cassava.task you wish to delete.");
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

    private static void saveTasks(List<Task> tasks) {
        try {
            putTasks(tasks);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

}
