package task;

public class EventTask extends Task {

    private String from;
    private String to;

    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (from: " + from + " to: " + to + ")";
    }

    public String getTaskTypeToken() {
        return "E";
    }
}
