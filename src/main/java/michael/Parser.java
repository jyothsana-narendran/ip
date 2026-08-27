package michael;

import task.Deadline;
import task.Todo;
import task.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles parsing and validation of user commands and parameters.
 */
public class Parser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public static int parseTaskIndex(String args, String commandName) throws MichaelException {
        if (args.isEmpty()) {
            throw new MichaelException("Please provide a task number after " + commandName + ".");
        }
        try {
            return Integer.parseInt(args) - 1;
        } catch (NumberFormatException e) {
            throw new MichaelException("task.Task numbers must be whole numbers.");
        }
    }

    public static Todo parseTodo(String args) throws MichaelException {
        if (args.isEmpty()) {
            throw new MichaelException("The description of a todo cannot be empty.");
        }
        return new Todo(args);
    }

    public static Deadline parseDeadline(String args) throws MichaelException {
        int byIndex = args.indexOf("/by");
        if (byIndex == -1) {
            throw new MichaelException("Please include a deadline using /by.");
        }

        String description = args.substring(0, byIndex).trim();
        String by = args.substring(byIndex + 3).trim();

        if (description.isEmpty()) {
            throw new MichaelException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new MichaelException("Please provide a time after /by.");
        }

        try {
            LocalDateTime deadline = LocalDateTime.parse(by, DATE_TIME_FORMATTER);
            return new Deadline(description, deadline);
        } catch (DateTimeParseException e) {
            throw new MichaelException(
                    "Please enter the deadline in the format yyyy-MM-dd HHmm, e.g. 2019-12-02 1800."
            );
        }
    }

    public static Event parseEvent(String args) throws MichaelException {
        int fromIndex = args.indexOf("/from");
        int toIndex = args.indexOf("/to");

        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
            throw new MichaelException("Please include an event time using /from and /to.");
        }

        String description = args.substring(0, fromIndex).trim();
        String from = args.substring(fromIndex + 5, toIndex).trim();
        String to = args.substring(toIndex + 3).trim();

        if (description.isEmpty()) {
            throw new MichaelException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new MichaelException("Please provide times after /from and /to.");
        }

        return new Event(description, from, to);
    }
}