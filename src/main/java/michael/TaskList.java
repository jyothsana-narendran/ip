package michael;

import task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the task list and handles list-manipulation operations.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) throws MichaelException {
        validateIndex(index);
        return tasks.remove(index);
    }

    public Task mark(int index) throws MichaelException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task unmark(int index) throws MichaelException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

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

    private void validateIndex(int index) throws MichaelException {
        if (index < 0 || index >= tasks.size()) {
            throw new MichaelException("That task number is not in the list.");
        }
    }
    /**
     * Finds and returns a list of tasks that contain the specified keyword in their description.
     *
     * @param keyword The substring to search for within task descriptions.
     * @return A list of tasks matching the keyword.
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}