package task;

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

    @Override
    public String toString() {
        return "[" + getTaskTypeToken() + "] " +
                "[" + (completed ? "X" : " ") + "] " +
                description;
    }

    public abstract String getTaskTypeToken();

    public String toDataString() {
        return this.getTaskTypeToken() + " | "
                + (this.completed ? "1": "0") +  " | "
                + this.description;
    }

    public void mark() {
        this.completed = true;
    }

    public void unmark() {
        this.completed = false;
    }
}
