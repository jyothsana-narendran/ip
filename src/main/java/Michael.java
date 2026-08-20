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
        Scanner input = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        String command = input.nextLine();

        while (!command.equals("bye")) {
            try {
                if (command.equals("list")) {
                    System.out.println(divider);
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(divider);
                } else if (command.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(command.substring(5));
                    tasks.get(taskNumber - 1).markAsDone();

                    System.out.println(divider);
                    System.out.println("Yay! You have finished this task:");
                    System.out.println("   [X] " + tasks.get(taskNumber - 1).getDescription());
                    System.out.println(divider);
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    tasks.get(taskNumber - 1).markAsNotDone();

                    System.out.println(divider);
                    System.out.println("This task is no longer marked as complete:");
                    System.out.println("   [ ] " + tasks.get(taskNumber - 1).getDescription());
                    System.out.println(divider);
                } else if (command.startsWith("delete ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    Task deletedTask = tasks.remove(taskNumber - 1);

                    System.out.println(divider);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + deletedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.substring(4).trim();
                    Task task = new Todo(description);
                    tasks.add(task);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = byIndex == -1
                            ? command.substring(8).trim() : command.substring(8, byIndex).trim();
                    String by = byIndex == -1 ? "" : command.substring(byIndex + 5);
                    Task task = new Deadline(description, by);
                    tasks.add(task);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = fromIndex == -1
                            ? command.substring(5).trim() : command.substring(5, fromIndex).trim();
                    String from = fromIndex == -1 || toIndex == -1
                            ? "" : command.substring(fromIndex + 7, toIndex);
                    String to = toIndex == -1 ? "" : command.substring(toIndex + 5);
                    Task task = new Event(description, from, to);
                    tasks.add(task);

                    System.out.println(divider);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(divider);
                } else {
                    throw new MichaelException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MichaelException e) {
                System.out.println(divider);
                System.out.println(e.getMessage());
                System.out.println(divider);
            }
            command = input.nextLine();
        }

        System.out.println(divider);
        System.out.println("Bye. Hope we meet again!");
        System.out.println(divider);
    }
}
