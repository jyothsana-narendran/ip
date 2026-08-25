import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts the Michael chatbot application.
 */
public class Michael {
    /**
     * Prints Michael's greeting and farewell, then exits the application.
     *
     * @param args command-line arguments, which are not used by this application
     */
    public static void main(String[] args) {
        String divider = "____________________________________________________________";

        System.out.println(divider);
        System.out.println("Hello! I'm Michael :)");
        System.out.println("How may I help you?");
        System.out.println(divider);
        ArrayList<Task> tasks;
        try {
            tasks = new ArrayList<>(Storage.load());
        } catch (IOException | MichaelException e) {
            printError(divider, "I could not load your saved tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String command = input.nextLine().trim();
            if (command.equals("bye")) {
                break;
            }
            try {
                if (command.equals("list")) {
                    System.out.println(divider);
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(divider);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(command, "mark", tasks);
                    tasks.get(taskNumber - 1).markAsDone();
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println("Yay! You have finished this task:");
                    System.out.println("   [X] " + tasks.get(taskNumber - 1).getDescription());
                    System.out.println(divider);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(command, "unmark", tasks);
                    tasks.get(taskNumber - 1).markAsNotDone();
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println("This task is no longer marked as complete:");
                    System.out.println("   [ ] " + tasks.get(taskNumber - 1).getDescription());
                    System.out.println(divider);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(command, "delete", tasks);
                    Task deletedTask = tasks.remove(taskNumber - 1);
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + deletedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.substring(4).trim();
                    Task task = new Todo(description);
                    tasks.add(task);
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    if (byIndex == -1) {
                        throw new MichaelException("Please include a deadline using /by.");
                    }
                    String description = command.substring(8, byIndex).trim();
                    String by = command.substring(byIndex + 5).trim();
                    if (by.isEmpty()) {
                        throw new MichaelException("Please provide a time after /by.");
                    }
                    Task task = new Deadline(description, by);
                    tasks.add(task);
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex + 7) {
                        throw new MichaelException("Please include an event time using /from and /to.");
                    }
                    String description = command.substring(5, fromIndex).trim();
                    String from = command.substring(fromIndex + 7, toIndex).trim();
                    String to = command.substring(toIndex + 5).trim();
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new MichaelException("Please provide times after /from and /to.");
                    }
                    Task task = new Event(description, from, to);
                    tasks.add(task);
                    Storage.save(tasks);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else {
                    throw new MichaelException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MichaelException | IOException e) {
                printError(divider, e.getMessage());
            }
        }

        System.out.println(divider);
        System.out.println("Bye. Hope we meet again!");
        System.out.println(divider);
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

    /**
     * Displays an error inside the chatbot's standard message border.
     *
     * @param divider the standard message border
     * @param message the error explanation
     */
    private static void printError(String divider, String message) {
        System.out.println(divider);
        System.out.println(" " + message);
        System.out.println(divider);
    }
}
