/**
 * Represents one task that can be marked as done or not done.
 */
public class Task {
    /** The icon that identifies the kind of task. */
    protected String type;

    /** The text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /** Extra date or time information displayed after the task description. */
    protected String timeDetails;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this("T", description, "");
    }

    /**
     * Creates an incomplete task with a type icon and optional time details.
     *
     * @param type the icon identifying the task type
     * @param description the text describing the task
     * @param timeDetails date or time information shown after the description
     */
    public Task(String type, String description, String timeDetails) {
        this.type = type;
        this.description = description;
        this.isDone = false;
        this.timeDetails = timeDetails;
    }

    /**
     * Returns the task description.
     *
     * @return the text describing this task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the icon used to display this task's completion status.
     *
     * @return {@code "X"} for a completed task, otherwise a space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the text used to display this task in a task list.
     *
     * @return the task type, status, description, and any time details
     */
    public String getDisplayText() {
        return "[" + type + "][" + getStatusIcon() + "] " + description + timeDetails;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }
}
