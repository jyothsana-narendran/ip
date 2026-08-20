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
        int taskCount = 0;
        String command = input.nextLine();

        while (!command.equals("bye")) {
            if (command.equals("list")) {
                System.out.println(divider);
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
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
