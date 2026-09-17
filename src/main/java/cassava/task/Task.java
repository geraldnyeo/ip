package cassava.task;

/**
 * Represents a single task item.
 */
public abstract class Task {
    public enum TaskPriority {
        UNKNOWN,
        HIGH,
        MEDIUM,
        LOW
    }

    public static String taskPriorityToString(TaskPriority taskPriority) {
        return switch (taskPriority) {
            case HIGH -> "high";
            case MEDIUM -> "medium";
            case LOW -> "low";
            default -> "unknown";
        };
    }

    public static TaskPriority stringToTaskPriority(String taskPriorityString) {
        return switch (taskPriorityString) {
            case "high" -> TaskPriority.HIGH;
            case "medium" -> TaskPriority.MEDIUM;
            case "low" -> TaskPriority.LOW;
            default -> TaskPriority.UNKNOWN;
        };
    }

    private String description;
    private boolean completed;
    private TaskPriority taskPriority;

    public Task(String description, boolean completed, TaskPriority taskPriority) {
        this.description = description;
        this.completed = completed;
        this.taskPriority = taskPriority;
    }

    public Task(String description) {
        this(description, false, TaskPriority.UNKNOWN);
    }

    public boolean equals(Task other) {
        return other.toDataString().equals(this.toDataString());
    }

    @Override
    public String toString() {
        return "[" + getTaskTypeToken() + "] "
                + "[" + (completed ? "X" : " ") + "] "
                + description
                + (this.taskPriority != TaskPriority.UNKNOWN
                ? " [" + taskPriorityToString(this.taskPriority) + "]"
                : "");
    }

    public abstract String getTaskTypeToken();

    public String toDataString() {
        return this.getTaskTypeToken() + " | "
                + (this.completed ? "1" : "0") + " | "
                + this.description + " | "
                + taskPriorityToString(this.taskPriority);
    }

    public String getDescription() {
        return this.description;
    }

    public void mark() {
        this.completed = true;
    }

    public void unmark() {
        this.completed = false;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }
}
