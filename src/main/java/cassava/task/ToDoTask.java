package cassava.task;

/**
 * Represents a ToDo-type task.
 */
public class ToDoTask extends Task {

    public ToDoTask(String description, boolean completed, TaskPriority taskPriority) {
        super(description, completed, taskPriority);
    }

    public ToDoTask(String description) {
        super(description);
    }

    @Override
    public String getTaskTypeToken() {
        return "T";
    }
}
