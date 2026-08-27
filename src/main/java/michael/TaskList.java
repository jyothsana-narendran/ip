package michael;

import task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the task list and handles list-manipulation operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Constructs an empty {@code TaskList}.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a {@code TaskList} initialized with a given list of tasks.
     *
     * @param tasks Initial list of tasks to populate the task list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a new task to the end of the list.
     *
     * @param task The task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The zero-based index of the task to delete.
     * @return The task that was removed.
     * @throws MichaelException If the specified index is out of bounds.
     */
    public Task delete(int index) throws MichaelException {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index The zero-based index of the task to unmark.
     * @return The task that was marked as incomplete.
     * @throws MichaelException If the specified index is out of bounds.
     */
    public Task mark(int index) throws MichaelException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Retrieves the task at the specified index without removing it.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index.
     * @throws MichaelException If the specified index is out of bounds.
     */
    public Task unmark(int index) throws MichaelException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return A {@code List} containing all tasks.
     */
    public Task get(int index) throws MichaelException {
        validateIndex(index);
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Validates whether a given index falls within the valid bounds of the task list.
     *
     * @param index The index to check.
     * @throws MichaelException If the index is negative or greater than/equal to the list size.
     */
    private void validateIndex(int index) throws MichaelException {
        if (index < 0 || index >= tasks.size()) {
            throw new MichaelException("That task number is not in the list.");
        }
    }
}