import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Saves and loads the chatbot's tasks from a file on disk.
 */
public class Storage {
    /** The location where task data is stored relative to the project root. */
    private static final Path FILE_PATH = Path.of("data", "michael.txt");

    /**
     * Writes every task in the current list to the storage file atomically.
     *
     * @param tasks the tasks to save
     * @throws IOException if the storage directory or file cannot be written
     */
    public static void save(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        Path temporaryFile = Files.createTempFile(FILE_PATH.getParent(), "michael-", ".tmp");
        try {
            Files.write(temporaryFile, tasks.stream().map(Storage::formatTask).toList());
            Files.move(temporaryFile, FILE_PATH, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException e) {
            Files.move(temporaryFile, FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    /**
     * Loads previously saved tasks, or returns an empty list when no data file exists yet.
     *
     * @return the tasks read from the storage file
     * @throws IOException if the storage file cannot be read
     * @throws MichaelException if a saved task cannot be recreated
     */
    public static List<Task> load() throws IOException, MichaelException {
        if (Files.notExists(FILE_PATH)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        List<String> lines = Files.readAllLines(FILE_PATH);
        for (int i = 0; i < lines.size(); i++) {
            try {
                tasks.add(createTask(lines.get(i)));
            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                throw new MichaelException("Unable to load task on line " + (i + 1) + ".");
            }
        }
        return tasks;
    }

    /**
     * Recreates one task from the line format produced by {@link #save(List)}.
     *
     * @param line one saved task
     * @return the recreated task
     * @throws MichaelException if the saved task has an invalid format or type
     */
    private static Task createTask(String line) throws MichaelException {
        if (line.startsWith("[")) {
            return createLegacyTask(line);
        }

        String[] fields = line.split("\\|", -1);
        if (fields.length < 3 || fields[0].length() != 1
                || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new MichaelException("Saved task has an invalid format.");
        }

        String taskType = fields[0];
        boolean isDone = fields[1].equals("1");
        Task task;

        if (taskType.equals("T")) {
            requireFieldCount(fields, 3);
            task = new Todo(decode(fields[2]));
        } else if (taskType.equals("D")) {
            requireFieldCount(fields, 4);
            task = new Deadline(decode(fields[2]), decode(fields[3]));
        } else if (taskType.equals("E")) {
            requireFieldCount(fields, 5);
            task = new Event(decode(fields[2]), decode(fields[3]), decode(fields[4]));
        } else {
            throw new MichaelException("Saved task has an unknown type.");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Recreates a task saved by the previous display-based storage format.
     *
     * @param line one legacy saved task
     * @return the recreated task
     * @throws MichaelException if the legacy task has an invalid format or type
     */
    private static Task createLegacyTask(String line) throws MichaelException {
        if (line.length() < 7 || line.charAt(0) != '[' || line.charAt(2) != ']'
                || line.charAt(3) != '[' || line.charAt(5) != ']' || line.charAt(6) != ' '
                || !(line.charAt(4) == ' ' || line.charAt(4) == 'X')) {
            throw new MichaelException("Saved task has an invalid format.");
        }

        String taskType = line.substring(1, 2);
        String details = line.substring(7);
        Task task;
        if (taskType.equals("T")) {
            task = new Todo(details);
        } else if (taskType.equals("D")) {
            int byIndex = details.lastIndexOf(" (by: ");
            if (byIndex < 0 || !details.endsWith(")")) {
                throw new MichaelException("Saved task has an invalid format.");
            }
            task = new Deadline(details.substring(0, byIndex),
                    details.substring(byIndex + 6, details.length() - 1));
        } else if (taskType.equals("E")) {
            int fromIndex = details.lastIndexOf(" (from: ");
            int toIndex = details.lastIndexOf(" to: ");
            if (fromIndex < 0 || toIndex <= fromIndex + 8 || !details.endsWith(")")) {
                throw new MichaelException("Saved task has an invalid format.");
            }
            task = new Event(details.substring(0, fromIndex),
                    details.substring(fromIndex + 8, toIndex),
                    details.substring(toIndex + 5, details.length() - 1));
        } else {
            throw new MichaelException("Saved task has an unknown type.");
        }

        if (line.charAt(4) == 'X') {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Converts a task into a delimiter-safe storage record.
     *
     * @param task the task to store
     * @return the task's storage record
     */
    private static String formatTask(Task task) {
        String status = task.getStatusIcon().equals("X") ? "1" : "0";
        if (task instanceof Todo) {
            return "T|" + status + "|" + encode(task.getDescription());
        } else if (task instanceof Deadline deadline) {
            return "D|" + status + "|" + encode(task.getDescription()) + "|" + encode(deadline.getBy());
        } else if (task instanceof Event event) {
            return "E|" + status + "|" + encode(task.getDescription()) + "|" + encode(event.getFrom())
                    + "|" + encode(event.getTo());
        }
        throw new IllegalArgumentException("Unsupported task type.");
    }

    /**
     * Checks that a storage record has the expected number of fields.
     *
     * @param fields the fields in the record
     * @param expectedCount the expected field count
     * @throws MichaelException if the record has an invalid field count
     */
    private static void requireFieldCount(String[] fields, int expectedCount) throws MichaelException {
        if (fields.length != expectedCount) {
            throw new MichaelException("Saved task has an invalid format.");
        }
    }

    /**
     * Encodes text so it can safely appear in a pipe-delimited storage record.
     *
     * @param text the text to encode
     * @return encoded text
     */
    private static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes text from a storage record.
     *
     * @param text encoded text
     * @return decoded text
     */
    private static String decode(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }
}
