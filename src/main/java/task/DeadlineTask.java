package task;

public class DeadlineTask extends Task {

    private String date;

    public DeadlineTask(String description, boolean completed, String date) {
        super(description, completed);
        this.date = date;
    }

    public DeadlineTask(String description, String date) {
        super(description);
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (by: " + date + ")";
    }

    @Override
    public String getTaskTypeToken() {
        return "D";
    }

    @Override
    public String toDataString() {
        return super.toDataString() + " | " + this.date;
    }
}
