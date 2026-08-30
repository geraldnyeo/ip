package cassava.task;

/**
 * Represents a single task item.
 */
public abstract class Task {
    private String description;
    private boolean completed;

    public Task(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }

    public Task(String description) {
        this(description, false);
    }

    public boolean equals(Task other) {
        return other.toDataString().equals(this.toDataString());
    }

    @Override
    public String toString() {
        return "[" + getTaskTypeToken() + "] "
                + "[" + (completed ? "X" : " ") + "] "
                + description;
    }

    public abstract String getTaskTypeToken();

    public String toDataString() {
        return this.getTaskTypeToken() + " | "
                + (this.completed ? "1" : "0") + " | "
                + this.description;
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
}
