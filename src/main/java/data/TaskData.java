package data;

import task.DeadlineTask;
import task.EventTask;
import task.Task;
import task.ToDoTask;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static data.TaskFileIO.openOrCreateTaskFile;
import static data.TaskFileIO.readTasks;
import static data.TaskFileIO.writeTasks;

public class TaskData {

    public static List<Task> getTasks() throws IOException, FileFormatException {
        Path path = openOrCreateTaskFile();
        List<String> taskStrings = readTasks(path);

        List<Task> tasks = new ArrayList<>();
        for (String taskString: taskStrings) {
            tasks.add(mapStringToTask(taskString));
        }

        return tasks;
    }

    public static void putTasks(List<Task> tasks) throws IOException {
        Path path = openOrCreateTaskFile();
        List<String> taskStrings = tasks.stream().map(TaskData::mapTaskToString).toList();
        writeTasks(path, taskStrings);
    }

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

    private static String mapTaskToString(Task task) {
        return task.toDataString();
    }
}
