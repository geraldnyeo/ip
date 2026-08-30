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

/**
 * Handles the commands input by the user.
 * Routes each command to the appropriate handler function,
 * which adds, updates or deletes the tasks as required.
 */
public class Handlers {

    /**
     * Routes the user input to the appropriate handler function.
     * Checks if the correct input parameters exist, if not,
     * routes to the invalid input handler.
     *
     * @param tasks List of tasks to update.
     * @param input_args Parsed user input command and parameters.
     * @param valid_cmds List of valid commands accepted by Cassava.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Prints list of tasks.
     * @param tasks List of tasks to read from.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks have been added yet...");
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }

        return false;
    }

    /**
     * Adds a ToDoTask.
     * @param tasks List of tasks to update.
     * @param description Description of the ToDoTask to be added.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Adds a DeadlineTask.
     * @param tasks List of tasks to update.
     * @param description Description of the DeadlineTask to be added.
     * @param byDateString Date by which the task should be completed
     *                     as a String in yyyy-MM-dd format.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Adds a EventTask
     * @param tasks List of tasks to update.
     * @param description Description of the EventTask to be added.
     * @param fromDateString Date from which the event will start
     *                       as a String in yyyy-MM-dd format.
     * @param toDateString Date to which the event will last
     *                     as a String in yyyy-MM-dd format.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Marks a task as completed.
     * @param tasks List of tasks to update.
     * @param index_arg Index of the task in the list to mark as completed.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Marks a task as incomplete.
     * @param tasks List of tasks to update.
     * @param index_arg Index of the task in the list to mark as incomplete.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Deletes a task.
     * @param tasks List of tasks to update.
     * @param index_arg Index of the task in the list to delete.
     * @return true If program should exit command loop Else false.
     */
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

    /**
     * Prints an error message when the user enters invalid input.
     * @param msg Error message to print.
     * @return false; the program should continue running
     */
    public static boolean handleInvalid(String msg) {
        System.out.println(msg);
        return false;
    }

    /**
     * Prints the goodbye message when the user exits the program.
     * @return true; the program should exit here
     */
    public static boolean handleExit() {
        System.out.println("Bye! See you again soon.");
        return true;
    }

    /**
     * Saves the updated tasks to a text file.
     * @param tasks List of tasks to be saved.
     */
    private static void saveTasks(List<Task> tasks) {
        try {
            putTasks(tasks);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

}
