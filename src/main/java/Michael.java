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
        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
        int taskCount = 0;
        String command = input.nextLine();

        while (!command.equals("bye")) {
            if (command.equals("list")) {
                System.out.println(divider);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = isDone[i] ? "[X]" : "[ ]";
                    System.out.println(" " + (i + 1) + "." + status + " " + tasks[i]);
                }
                System.out.println(divider);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                isDone[taskNumber - 1] = true;

                System.out.println(divider);
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   [X] " + tasks[taskNumber - 1]);
                System.out.println(divider);
            } else {
                tasks[taskCount] = command;
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
