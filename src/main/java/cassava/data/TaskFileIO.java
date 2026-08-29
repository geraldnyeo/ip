package cassava.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TaskFileIO {

    public static final String DATAPATH = "./data/tasks.txt";

    public static Path openOrCreateTaskFile() throws IOException {
        Path path = Paths.get(DATAPATH);
        Path parentDirectory = path.getParent();

        if (!Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }

        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        return path;
    }

    public static List<String> readTasks(Path path) throws IOException {
        List<String> tasks = Files.readAllLines(path);
        return tasks;
    }

    public static void writeTasks(Path path, List<String> tasks) throws IOException {
        Files.write(path, tasks);
    }

}
