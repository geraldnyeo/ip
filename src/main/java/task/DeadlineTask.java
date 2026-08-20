package task;

public class DeadlineTask extends Task {

    private String date;

    public DeadlineTask(String description, String date) {
        super(description);
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() +
                " (by: " + date + ")";
    }

    public String getTaskTypeToken() {
        return "D";
    }
}
