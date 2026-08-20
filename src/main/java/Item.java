public class Item {
    private String description;
    private boolean completed;

    public Item(String description) {
        this.description = description;
        this.completed = false;
    }

    @Override
    public String toString() {
        return "[" + (completed ? "X" : " ") + "] " + description;
    }

    public void mark() {
        this.completed = true;
    }

    public void unmark() {
        this.completed = false;
    }
}
