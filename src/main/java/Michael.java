import java.io.IOException;

/**
 * Starts the Michael chatbot application.
 */
public class Michael {
    private final Storage storage;
    private final Ui ui;
    private TaskList list;

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
                    default:
                        throw new MichaelException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MichaelException | IOException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Michael("data/michael.txt").run();
    }
}