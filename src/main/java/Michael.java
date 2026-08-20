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
        Task[] tasks = new Task[100];
        int taskCount = 0;
        String command = input.nextLine();

        while (!command.equals("bye")) {
            if (command.equals("list")) {
                System.out.println(divider);
                System.out.println("This is your list of tasks:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ".[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
                System.out.println(divider);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                tasks[taskNumber - 1].markAsDone();

                System.out.println(divider);
                System.out.println("Yay! You have finished this task:");
                System.out.println("   [X] " + tasks[taskNumber - 1].getDescription());
                System.out.println(divider);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                tasks[taskNumber - 1].markAsNotDone();

                System.out.println(divider);
                System.out.println("This task is no longer marked as complete:");
                System.out.println("   [ ] " + tasks[taskNumber - 1].getDescription());
                System.out.println(divider);
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;

                System.out.println(divider);
                System.out.println(" added: " + command);
                System.out.println(divider);
            }
            command = input.nextLine();
        }

        System.out.println(divider);
        System.out.println("Bye. Hope we meet again!");
        System.out.println(divider);
    }
}
