package cassava.data;

import cassava.task.DeadlineTask;
import cassava.task.EventTask;
import cassava.task.Task;
import cassava.task.ToDoTask;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cassava.data.TaskFileIO.openOrCreateTaskFile;
import static cassava.data.TaskFileIO.readTasks;
import static cassava.data.TaskFileIO.writeTasks;

/**
 * Handles parsing and formatting of data,
 * acts as the connection layer between FileIO and the application.
 */
public class TaskData {

    /**
     * Gets the list of tasks from the data file.
     * @return List of tasks retrieved.
     * @throws IOException
     * @throws FileFormatException If the file is corrupted / cannot be parsed.
     */
    public static List<Task> getTasks() throws IOException, FileFormatException {
        Path path = openOrCreateTaskFile();
        List<String> taskStrings = readTasks(path);

        List<Task> tasks = new ArrayList<>();
        for (String taskString: taskStrings) {
            tasks.add(mapStringToTask(taskString));
        }

        return tasks;
    }

    /**
     * Saves the list of tasks to the data file.
     * @param tasks List of tasks to save.
     * @throws IOException
     */
    public static void putTasks(List<Task> tasks) throws IOException {
        Path path = openOrCreateTaskFile();
        List<String> taskStrings = tasks.stream().map(TaskData::mapTaskToString).toList();
        writeTasks(path, taskStrings);
    }

    /**
     * Parses a line of text from the data file into a Task object.
     * @param taskString raw text line from data file representing a task.
     * @return Task object
     * @throws FileFormatException If the line is corrupted / cannot be parsed.
     */
    private static Task mapStringToTask(String taskString) throws FileFormatException {
        String[] tokens = taskString.split(" \\| ");
        String taskType = tokens[0];
        boolean taskCompleted = tokens[1].equals("1");

        return switch (taskType) {
            case "T" -> new ToDoTask(tokens[2], taskCompleted);
            case "D" -> new DeadlineTask(tokens[2], taskCompleted, LocalDate.parse(tokens[3]));
            case "E" -> new EventTask(tokens[2], taskCompleted, LocalDate.parse(tokens[3]), LocalDate.parse(tokens[4]));
            default -> throw new FileFormatException("Unrecognized Task type: " + taskType);
        };
    }

    /**
     * Converts a task to its String data representation for storage.
     * @param task Task object to convert to a String.
     * @return String data representation of the task.
     */
    private static String mapTaskToString(Task task) {
        return task.toDataString();
    }
}
