package task; /**
 * Represents a task that should be completed by a specified time.
 */
import michael.MichaelException;

import java.time.LocalDateTime;

public class Deadline extends Task {
    /** The date and time by which the task should be completed. */
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) throws MichaelException {
        super(description, "deadline");
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by.format(
                java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"))
                + ")";
    }
}