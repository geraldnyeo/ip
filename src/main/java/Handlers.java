import task.DeadlineTask;
import task.EventTask;
import task.Task;
import task.ToDoTask;

import java.util.HashMap;
import java.util.List;

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

}
