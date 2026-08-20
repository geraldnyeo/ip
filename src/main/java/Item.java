public class Item {
    private String description;

    public Item(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
