package task;

public class ToDoTask extends Task {

    public ToDoTask(String description) {
        super(description);
    }

    @Override
    public String getTaskTypeToken() {
        return "T";
    }
}
