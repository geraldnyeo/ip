package cassava;

import static cassava.data.TaskData.putTasks;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import cassava.gui.MainWindow;
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
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param inputArgs Parsed user input command and parameters.
     * @param validCmds List of valid commands accepted by Cassava.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleInput(
            MainWindow mainWindow,
            List<Task> tasks,
            HashMap<String, String> inputArgs,
            List<String> validCmds
    ) {
        assert mainWindow != null;
        assert tasks != null;

        String command = inputArgs.getOrDefault("command", "");
        String commandOption = inputArgs.getOrDefault(command, "");
        if (command.isEmpty()) {
            return handleInvalid(mainWindow, "You have not entered any text.");
        }
        if (!validCmds.contains(command)) {
            return handleInvalid(mainWindow, "Sorry, I don't recognise this command.");
        }

        return switch (command) {
            case "list" -> handleList(mainWindow, tasks);
            case "find" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a search term to find.")
                    : handleFind(mainWindow, tasks, commandOption);
            case "todo" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow,"You did not specify a cassava.task to add.")
                    : handleAddTodo(mainWindow, tasks, commandOption);
            case "deadline" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a cassava.task to add.")
                    : !inputArgs.containsKey("by")
                    ? handleInvalid(mainWindow, "You did not specify a date for the deadline.")
                    : handleAddDeadline(mainWindow, tasks, commandOption, inputArgs.get("by"));
            case "event" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a cassava.task to add.")
                    : !inputArgs.containsKey("from")
                    ? handleInvalid(mainWindow, "You did not specify a time for 'from'.")
                    : !inputArgs.containsKey("to")
                    ? handleInvalid(mainWindow, "You did not specify a time for 'to'.")
                    : handleAddEvent(mainWindow, tasks, commandOption, inputArgs.get("from"), inputArgs.get("to"));
            case "mark" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a cassava.task to mark.")
                    : handleMark(mainWindow, tasks, commandOption);
            case "unmark" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a cassava.task to unmark.")
                    : handleUnmark(mainWindow, tasks, commandOption);
            case "delete" -> commandOption.isEmpty()
                    ? handleInvalid(mainWindow, "You did not specify a cassava.task to delete.")
                    : handleDelete(mainWindow, tasks, commandOption);
            case "bye" -> handleExit(mainWindow);
            default -> handleInvalid(mainWindow, "Sorry, I don't recognise this command.");
        };
    }

    /**
     * Prints list of tasks.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to read from.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleList(
            MainWindow mainWindow,
            List<Task> tasks
    ) {
        assert mainWindow != null;
        assert tasks != null;

        if (tasks.isEmpty()) {
            mainWindow.addCassavaDialog("No tasks have been added yet...");
        }

        for (int i = 0; i < tasks.size(); i++) {
            mainWindow.addCassavaDialog((i + 1) + ". " + tasks.get(i).toString());
        }

        return false;
    }

    /**
     * Filters and prints tasks according to the search term.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to search from.
     * @param search Search term to search for.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleFind(
            MainWindow mainWindow,
            List<Task> tasks,
            String search
    ) {
        assert mainWindow != null;
        assert tasks != null;

        List<Task> filtered = tasks.stream()
                .filter(s -> s.getDescription().contains(search))
                .toList();

        if (filtered.isEmpty()) {
            mainWindow.addCassavaDialog("No tasks were found...");
        } else {
            mainWindow.addCassavaDialog("Here are the matching tasks I found:");
        }

        for (int i = 0; i < filtered.size(); ++i) {
            mainWindow.addCassavaDialog((i + 1) + ". " + filtered.get(i).toString());
        }

        return false;
    }

    /**
     * Adds a ToDoTask.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param description Description of the ToDoTask to be added.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleAddTodo(
            MainWindow mainWindow,
            List<Task> tasks,
            String description
    ) {
        assert mainWindow != null;
        assert tasks != null;

        Task task = new ToDoTask(description);
        tasks.add(task);
        saveTasks(tasks);

        mainWindow.addCassavaDialog("Added: " + description);

        return false;
    }

    /**
     * Adds a DeadlineTask.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param description Description of the DeadlineTask to be added.
     * @param byDateString Date by which the task should be completed
     *                     as a String in yyyy-MM-dd format.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleAddDeadline(
            MainWindow mainWindow,
            List<Task> tasks,
            String description,
            String byDateString
    ) {
        assert mainWindow != null;
        assert tasks != null;

        LocalDate byDate = LocalDate.now();
        try {
            byDate = LocalDate.parse(byDateString);
        } catch (DateTimeParseException e) {
            return handleInvalid(mainWindow, "You must input a date in the format 'yyyy-mm-dd' for the 'by' argument.");
        }

        Task task = new DeadlineTask(description, byDate);
        tasks.add(task);
        saveTasks(tasks);

        mainWindow.addCassavaDialog("Added cassava.task: " + task.toString());

        return false;
    }

    /**
     * Adds a EventTask
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param description Description of the EventTask to be added.
     * @param fromDateString Date from which the event will start
     *                       as a String in yyyy-MM-dd format.
     * @param toDateString Date to which the event will last
     *                     as a String in yyyy-MM-dd format.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleAddEvent(
            MainWindow mainWindow,
            List<Task> tasks,
            String description,
            String fromDateString,
            String toDateString
    ) {
        assert mainWindow != null;
        assert tasks != null;

        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        try {
            fromDate = LocalDate.parse(fromDateString);
            toDate = LocalDate.parse(toDateString);
        } catch (DateTimeParseException e) {
            return handleInvalid(mainWindow, "You must input a date in the format 'yyyy-mm-dd' "
                    + "for the 'from' and 'by arguments.");
        }

        Task task = new EventTask(description, fromDate, toDate);
        tasks.add(task);
        saveTasks(tasks);

        mainWindow.addCassavaDialog("Added cassava.task: " + task.toString());

        return false;
    }

    /**
     * Marks a task as completed.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param indexArg Index of the task in the list to mark as completed.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleMark(
            MainWindow mainWindow,
            List<Task> tasks,
            String indexArg
    ) {
        assert mainWindow != null;
        assert tasks != null;

        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid(mainWindow, "There is no such cassava.task, I cannot mark it.");
            }
            tasks.get(index).mark();
            saveTasks(tasks);

            mainWindow.addCassavaDialog(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid(mainWindow, "Please use only numbers to specify the cassava.task you wish to mark.");
        }
    }

    /**
     * Marks a task as incomplete.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param indexArg Index of the task in the list to mark as incomplete.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleUnmark(
            MainWindow mainWindow,
            List<Task> tasks,
            String indexArg
    ) {
        assert mainWindow != null;
        assert tasks != null;

        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid(mainWindow, "There is no such cassava.task, I cannot unmark it.");
            }
            tasks.get(index).unmark();
            saveTasks(tasks);

            mainWindow.addCassavaDialog(tasks.get(index).toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid(mainWindow, "Please use only numbers to specify the cassava.task you wish to mark.");
        }
    }

    /**
     * Deletes a task.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param tasks List of tasks to update.
     * @param indexArg Index of the task in the list to delete.
     * @return true If program should exit command loop Else false.
     */
    public static boolean handleDelete(
            MainWindow mainWindow,
            List<Task> tasks,
            String indexArg
    ) {
        assert mainWindow != null;
        assert tasks != null;

        try {
            int index = Integer.parseInt(indexArg) - 1;
            if (index >= tasks.size()) {
                return handleInvalid(mainWindow, "There is no such Task, I cannot delete it.");
            }
            Task task = tasks.remove(index);
            saveTasks(tasks);

            mainWindow.addCassavaDialog(task.toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid(mainWindow, "Please use only numbers to specify the cassava.task you wish to delete.");
        }
    }

    /**
     * Prints an error message when the user enters invalid input.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @param msg Error message to print.
     * @return false; the program should continue running
     */
    public static boolean handleInvalid(
            MainWindow mainWindow,
            String msg
    ) {
        assert mainWindow != null;

        mainWindow.addCassavaDialog(msg);
        return false;
    }

    /**
     * Prints the goodbye message when the user exits the program.
     * @param mainWindow Reference to mainWindow for executing UI functions.
     * @return true; the program should exit here
     */
    public static boolean handleExit(MainWindow mainWindow) {
        assert mainWindow != null;

        mainWindow.addCassavaDialog("Bye! See you again soon.");
        return true;
    }

    /**
     * Saves the updated tasks to a text file.
     * @param tasks List of tasks to be saved.
     */
    private static void saveTasks(List<Task> tasks) {
        assert tasks != null;

        try {
            putTasks(tasks);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

}
