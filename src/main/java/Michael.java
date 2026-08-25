import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Starts the Michael chatbot application.
 */
public class Michael {

    /**
     * Starts the Michael chatbot and coordinates user commands, tasks, and storage.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        ArrayList<Task> tasks;
        try {
            tasks = new ArrayList<>(Storage.load());
        } catch (IOException | MichaelException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new ArrayList<>();
        }

        String command;
        while ((command = ui.readCommand()) != null) {
            if (command.equals("bye")) {
                break;
            }
            try {
                if (command.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(command, "mark", tasks);
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    Storage.save(tasks);

                    ui.showTaskMarked(task);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(command, "unmark", tasks);
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();
                    Storage.save(tasks);

                    ui.showTaskUnmarked(task);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(command, "delete", tasks);
                    Task deletedTask = tasks.remove(taskNumber - 1);
                    Storage.save(tasks);

                    ui.showTaskDeleted(deletedTask, tasks.size());
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new MichaelException("The description of a todo cannot be empty.");
                    }
                    Task task = new Todo(description);
                    tasks.add(task);
                    Storage.save(tasks);

                    ui.showTaskAdded(task, tasks.size());
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    if (byIndex == -1) {
                        throw new MichaelException("Please include a deadline using /by.");
                    }
                    String description = command.substring(8, byIndex).trim();
                    String by = command.substring(byIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new MichaelException("The description of a deadline cannot be empty.");
                    }
                    if (by.isEmpty()) {
                        throw new MichaelException("Please provide a time after /by.");
                    }
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                        LocalDateTime deadline = LocalDateTime.parse(by, formatter);

                        Task task = new Deadline(description, deadline);
                        tasks.add(task);
                        Storage.save(tasks);

                        ui.showTaskAdded(task, tasks.size());
                    } catch (DateTimeParseException e) {
                        throw new MichaelException(
                                "Please enter the deadline in the format yyyy-MM-dd HHmm, e.g. 2019-12-02 1800."
                        );
                    }
                } else if (command.equals("event") || command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex + 7) {
                        throw new MichaelException("Please include an event time using /from and /to.");
                    }
                    String description = command.substring(5, fromIndex).trim();
                    String from = command.substring(fromIndex + 7, toIndex).trim();
                    String to = command.substring(toIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new MichaelException("The description of an event cannot be empty.");
                    }
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new MichaelException("Please provide times after /from and /to.");
                    }
                    Task task = new Event(description, from, to);
                    tasks.add(task);
                    Storage.save(tasks);

                    ui.showTaskAdded(task, tasks.size());
                } else {
                    throw new MichaelException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MichaelException | IOException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    /**
     * Gets and validates the task number following a list-changing command.
     *
     * @param command the full user command
     * @param commandName the command name
     * @param tasks the current task list
     * @return the validated one-based task number
     * @throws MichaelException if the task number is missing, invalid, or out of range
     */
    private static int getTaskNumber(String command, String commandName, ArrayList<Task> tasks)
            throws MichaelException {
        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new MichaelException("Please provide a task number after " + commandName + ".");
        }

        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new MichaelException("That task number is not in the list.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new MichaelException("Task numbers must be whole numbers.");
        }
    }
}