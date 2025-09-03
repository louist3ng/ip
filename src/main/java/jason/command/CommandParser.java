package jason.command;

import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;

/**
 * Parses user input into command objects.
 */
public final class CommandParser {
    /**
     * Private constructor to prevent instantiation.
     */
    private CommandParser() {}

    /**
     * Parses the given input string into a Command object.
     *
     * @param input the input string to parse
     * @return the corresponding Command object
     * @throws IncorrectInputException if the input is invalid
     */
    public static Command parse(String input) {
        String trimmed = (input == null) ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new IncorrectInputException();
        }

        String[] headTail = trimmed.split("\\s+", 2);
        String cmd = headTail[0].toLowerCase();
        String description = (headTail.length > 1) ? headTail[1] : "";

        if (cmd.equals("bye")) {
            if (!description.isEmpty()) {
                throw new IncorrectInputException();
            }
            return new ByeCommand();

        } else if (cmd.equals("list")) {
            if (!description.isEmpty()) {
                throw new IncorrectInputException();
            }
            return new ListCommand();

        } else if (cmd.equals("todo")) {
            return new TodoCommand(description);

        } else if (cmd.equals("deadline")) {
            String lower = description.toLowerCase();
            int byIdx = lower.indexOf("/by");
            if (byIdx < 0) {
                throw new IncorrectInputException();
            }
            String desc = description.substring(0, byIdx).trim();
            String byStr = description.substring(byIdx + 3).trim();
            return new DeadlineCommand(desc, byStr);

        } else if (cmd.equals("event")) {
            String lower = description.toLowerCase();
            int fromIdx = lower.indexOf("/from");
            int toIdx = lower.indexOf("/to", Math.max(fromIdx, 0) + 5);
            if (fromIdx < 0 || toIdx < 0) {
                throw new IncorrectInputException();
            }
            String desc = description.substring(0, fromIdx).trim();
            String fromS = description.substring(fromIdx + 5, toIdx).trim();
            String toS = description.substring(toIdx + 3).trim();
            return new EventCommand(desc, fromS, toS);

        } else if (cmd.equals("mark")) {
            return new MarkCommand(parseIndex(description));

        } else if (cmd.equals("unmark")) {
            return new UnmarkCommand(parseIndex(description));

        } else if (cmd.equals("delete")) {
            return new DeleteCommand(parseIndex(description));

        } else if (cmd.equals("find")) {
            if (description.trim().isEmpty()) {
                throw new EmptyException();
            }
            return new FindCommand(description);

        } else {
            throw new IncorrectInputException();
        }
    }

    /**
     * Parses a one-based index from the given string.
     *
     * @param s the string to parse
     * @return the zero-based index
     * @throws EmptyException if the string is empty
     * @throws IncorrectInputException if the string is not a valid integer
     */
    private static int parseIndex(String s) {
        String t = (s == null) ? "" : s.replaceAll("\\s+", "");
        if (t.isEmpty()) {
            throw new EmptyException();
        }
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            throw new IncorrectInputException();
        }
    }
}
