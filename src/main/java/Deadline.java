/**
 * Represents a task that should be completed by a specified time.
 */
public class Deadline extends Task {
    /** The time by which the task should be completed. */
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the text describing the task
     * @param by the time by which the task should be completed
     * @throws MichaelException if the task description is empty
     */
    public Deadline(String description, String by) throws MichaelException {
        super(description, "deadline");
        this.by = by;
    }

    /**
     * Returns the text used to display this deadline task.
     *
     * @return the deadline task type, status, description, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
