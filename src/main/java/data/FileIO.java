package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {

    public static final String dataPath = "data/tasks.json";

    public static Path openOrCreateSaveFile() throws IOException {
        Path path = Paths.get(dataPath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }

}
