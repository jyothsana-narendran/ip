/**
 * Represents one task that can be marked as done or not done.
 */
public class Task {
    /** The text describing this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description and type.
     *
     * @param description the text describing the task
     * @param taskType the human-readable type used in validation messages
     * @throws MichaelException if the task description is empty
     */
    protected Task(String description, String taskType) throws MichaelException {
        if (description == null || description.isBlank()) {
            String article = taskType.equals("event") ? "an" : "a";
            throw new MichaelException(" Oh No! The description of " + article + " "
                    + taskType + " is empty. Please add a description after the task type");
        }
        this.description = description;
        this.isDone = false;
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

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task's status and description.
     *
     * @return the text used to display a basic task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
