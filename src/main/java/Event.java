/**
 * Represents a task that happens between a start and end time.
 */
public class Event extends Task {
    /** The event start time. */
    protected String from;

    /** The event end time. */
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description the text describing the event
     * @param from the event start time
     * @param to the event end time
     * @throws MichaelException if the task description is empty
     */
    public Event(String description, String from, String to) throws MichaelException {
        super(description, "event");
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the text used to display this event task.
     *
     * @return the event task type, status, description, and time range
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
