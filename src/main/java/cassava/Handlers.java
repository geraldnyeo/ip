package cassava;

import cassava.task.DeadlineTask;
import cassava.task.EventTask;
import cassava.task.Task;
import cassava.task.ToDoTask;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import static cassava.data.TaskData.putTasks;

public class Handlers {

    public static boolean handleInput(
            List<Task> tasks,
            HashMap<String, String> input_args,
            List<String> valid_cmds
    ) {
        String command = input_args.getOrDefault("command", "");
        String command_option = input_args.getOrDefault(command, "");
        if (command.isEmpty()) {
            return handleInvalid("You have not entered any text.");
        }
        if (!valid_cmds.contains(command)) {
            return handleInvalid("Sorry, I don't recognise this command.");
        }

        return switch (command) {
            case "list" -> handleList(tasks);
            case "find" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a search term to find.")
                    : handleFind(tasks, command_option);
            case "todo" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : handleAddTodo(tasks, command_option);
            case "deadline" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : !input_args.containsKey("by")
                    ? handleInvalid("You did not specify a date for the deadline.")
                    : handleAddDeadline(tasks, command_option, input_args.get("by"));
            case "event" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to add.")
                    : !input_args.containsKey("from")
                    ? handleInvalid("You did not specify a time for 'from'.")
                    : !input_args.containsKey("to")
                    ? handleInvalid("You did not specify a time for 'to'.")
                    : handleAddEvent(tasks, command_option, input_args.get("from"), input_args.get("to"));
            case "mark" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to mark.")
                    : handleMark(tasks, command_option);
            case "unmark" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to unmark.")
                    : handleUnmark(tasks, command_option);
            case "delete" -> command_option.isEmpty()
                    ? handleInvalid("You did not specify a cassava.task to delete.")
                    : handleDelete(tasks, command_option);
            case "bye" -> handleExit();
            default -> handleInvalid("Sorry, I don't recognise this command.");
        };
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

    public static boolean handleFind(List<Task> tasks, String search) {
        List<Task> filtered = tasks.stream()
                .filter(s -> s.getDescription().contains(search))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No tasks were found...");
        } else {
            System.out.println("Here are the matching tasks I found:");
        }

        for (int i = 0; i < filtered.size(); ++i) {
            System.out.println((i + 1) + ". " + filtered.get(i).toString());
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
            return handleInvalid("You must input a date in the format 'yyyy-mm-dd' " +
                    "for the 'from' and 'by arguments.");
        }

        Task task = new EventTask(description, fromDate, toDate);
        tasks.add(task);
        saveTasks(tasks);

        System.out.println("Added cassava.task: " + task.toString());

        return false;
    }

    public static boolean handleMark(
            List<Task> tasks,
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
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
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
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
            String index_arg
    ) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
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
