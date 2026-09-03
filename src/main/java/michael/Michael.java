package michael;

import task.Task;
import java.io.IOException;
import java.util.List;

/**
 * Starts the michael.Michael chatbot application.
 */
public class Michael {
    private final Storage storage;
    private final Ui ui;
    private TaskList list;

    /**
     * Initializes the chatbot components and loads previously saved tasks from disk.
     * If loading fails due to an exception, an empty task list is initialized.
     *
     * @param filePath The file path where tasks are saved and loaded from.
     */
    public Michael(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            list = new TaskList(storage.load());
        } catch (IOException | MichaelException e) {
            ui.showLoadingError(e.getMessage());
            list = new TaskList();
        }
    }

    /**
     * Runs the main execution loop of the application.
     * Continually reads commands from the user, executes requested task operations,
     * updates storage, and outputs status messages until the "bye" command is given.
     */
    public void run() {
        ui.showWelcome();

        String command;
        while ((command = ui.readCommand()) != null) {
            if (command.equals("bye")) {
                break;
            }

            String[] parts = command.split(" ", 2);
            String commandWord = parts[0];
            String commandArgs = parts.length > 1 ? parts[1].trim() : "";

            try {
                switch (commandWord) {
                    case "list":
                        ui.showTaskList(list.getTasks());
                        break;

                    case "mark": {
                        int index = Parser.parseTaskIndex(commandArgs, "mark");
                        Task task = list.mark(index);
                        storage.save(list);
                        ui.showTaskMarked(task);
                        break;
                    }
                    case "unmark": {
                        int index = Parser.parseTaskIndex(commandArgs, "unmark");
                        Task task = list.unmark(index);
                        storage.save(list);
                        ui.showTaskUnmarked(task);
                        break;
                    }
                    case "delete": {
                        int index = Parser.parseTaskIndex(commandArgs, "delete");
                        Task deletedTask = list.delete(index);
                        storage.save(list);
                        ui.showTaskDeleted(deletedTask, list.size());
                        break;
                    }
                    case "todo": {
                        Task task = Parser.parseTodo(commandArgs);
                        list.add(task);
                        storage.save(list);
                        ui.showTaskAdded(task, list.size());
                        break;
                    }
                    case "deadline": {
                        Task task = Parser.parseDeadline(commandArgs);
                        list.add(task);
                        storage.save(list);
                        ui.showTaskAdded(task, list.size());
                        break;
                    }
                    case "event": {
                        Task task = Parser.parseEvent(commandArgs);
                        list.add(task);
                        storage.save(list);
                        ui.showTaskAdded(task, list.size());
                        break;
                    }
                    case "find": {
                        String keyword = Parser.parseFind(commandArgs);
                        List<Task> matchingTasks = list.find(keyword);
                        ui.showMatchingTasks(matchingTasks);
                        break;
                    }
                    default:
                        throw new MichaelException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MichaelException | IOException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    /** Executes one user command and returns the message shown in the GUI. */
    public String processCommand(String command) {
        String[] parts = command.trim().split(" ", 2);
        String commandWord = parts[0];
        String commandArgs = parts.length > 1 ? parts[1].trim() : "";
        try {
            switch (commandWord) {
                case "list":
                    if (list.getTasks().isEmpty()) return "Your task list is empty.";
                    StringBuilder result = new StringBuilder("Here are your tasks:\n");
                    for (int i = 0; i < list.getTasks().size(); i++) {
                        result.append(i + 1).append(". ").append(list.getTasks().get(i)).append('\n');
                    }
                    return result.toString().trim();
                case "mark":
                    Task marked = list.mark(Parser.parseTaskIndex(commandArgs, "mark"));
                    storage.save(list);
                    return "Marked as done:\n" + marked;
                case "unmark":
                    Task unmarked = list.unmark(Parser.parseTaskIndex(commandArgs, "unmark"));
                    storage.save(list);
                    return "Marked as not done:\n" + unmarked;
                case "delete":
                    Task deleted = list.delete(Parser.parseTaskIndex(commandArgs, "delete"));
                    storage.save(list);
                    return "Deleted:\n" + deleted;
                case "todo":
                    Task todo = Parser.parseTodo(commandArgs);
                    list.add(todo);
                    storage.save(list);
                    return "Added:\n" + todo;
                case "deadline":
                    Task deadline = Parser.parseDeadline(commandArgs);
                    list.add(deadline);
                    storage.save(list);
                    return "Added:\n" + deadline;
                case "event":
                    Task event = Parser.parseEvent(commandArgs);
                    list.add(event);
                    storage.save(list);
                    return "Added:\n" + event;
                case "find":
                    List<Task> matches = list.find(Parser.parseFind(commandArgs));
                    return matches.isEmpty() ? "No matching tasks found." : matches.toString();
                case "bye":
                    return "Bye. Hope we meet again!";
                default:
                    throw new MichaelException("Sorry, I don't know what that means.");
            }
        } catch (MichaelException | IOException e) {
            return e.getMessage();
        }
    }

    /** Generates a response for a user's chat message. */
    public String getResponse(String input) {
        return processCommand(input);
    }

    /**
     * Main entry point for starting the Michael application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Michael("data/michael.txt").run();
    }
}
