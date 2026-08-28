package cassava.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeadlineTask extends Task {

    private LocalDate date;

    public DeadlineTask(String description, boolean completed, LocalDate date) {
        super(description, completed);
        this.date = date;
    }

    public DeadlineTask(String description, LocalDate date) {
        super(description);
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " +
                this.date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    @Override
    public String getTaskTypeToken() {
        return "D";
    }

    @Override
    public String toDataString() {
        return super.toDataString() + " | " +
                this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
