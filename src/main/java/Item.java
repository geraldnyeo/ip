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
}
