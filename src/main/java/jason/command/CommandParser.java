package jason.command;

import jason.exception.EmptyException;
import jason.exception.IncorrectInputException;

/** Parses user input into command objects. */
public final class CommandParser {
    // Command argument keywords (case-insensitive)
    private static final String BY = "/by";
    private static final String FROM = "/from";
    private static final String TO = "/to";

    private CommandParser() {}

    /**
     * Parses the given input string into a Command object.
     *
     * @param input the input string to parse
     * @return the corresponding Command object
     * @throws IncorrectInputException if the input is invalid
     */
    public static Command parse(String input) {
        final String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new IncorrectInputException();
        }

        final String[] headTail = trimmed.split("\\s+", 2);
        final String cmd = headTail[0].toLowerCase();
        final String args = (headTail.length > 1) ? headTail[1] : "";

        switch (cmd) {
            case "bye"      -> {
                ensureNoArgs(args);
                return new ByeCommand();
            }
            case "list"     -> {
                ensureNoArgs(args);
                return new ListCommand();
            }
            case "todo"     -> {
                return parseTodo(args);
            }
            case "deadline" -> {
                return parseDeadline(args);
            }
            case "event"    -> {
                return parseEvent(args);
            }
            case "mark"     -> {
                return new MarkCommand(parseIndexOneBased(args));
            }
            case "unmark"   -> {
                return new UnmarkCommand(parseIndexOneBased(args));
            }
            case "delete"   -> {
                return new DeleteCommand(parseIndexOneBased(args));
            }
            case "find"     -> {
                return parseFind(args);
            }
            case "help"     -> {
                return new HelpCommand(args);
            }
            default         -> throw new IncorrectInputException();
        }
    }


    private static void ensureNoArgs(String args) {
        if (!args.isBlank()) {
            throw new IncorrectInputException();
        }
    }

    private static Command parseTodo(String args) {
        final String desc = args.strip();
        if (desc.isEmpty()) {
            throw new EmptyException();
        }
        return new TodoCommand(desc);
    }

    private static Command parseFind(String args) {
        final String keyword = args.strip();
        if (keyword.isEmpty()) {
            throw new EmptyException();
        }
        return new FindCommand(keyword);
    }

    private static Command parseDeadline(String args) {
        final int byIdx = indexOfIgnoreCase(args, BY);
        if (byIdx < 0) {
            throw new IncorrectInputException();
        }
        final String desc = args.substring(0, byIdx).trim();
        final String byStr = args.substring(byIdx + BY.length()).trim();
        if (desc.isEmpty() || byStr.isEmpty()) {
            throw new IncorrectInputException();
        }
        return new DeadlineCommand(desc, byStr);
    }

    private static Command parseEvent(String args) {
        final int fromIdx = indexOfIgnoreCase(args, FROM);
        final int toIdx = indexOfIgnoreCase(args, TO, Math.max(fromIdx, 0) + FROM.length());
        if (fromIdx < 0 || toIdx < 0) {
            throw new IncorrectInputException();
        }
        final String desc  = args.substring(0, fromIdx).trim();
        final String fromS = args.substring(fromIdx + FROM.length(), toIdx).trim();
        final String toS   = args.substring(toIdx + TO.length()).trim();
        if (desc.isEmpty() || fromS.isEmpty() || toS.isEmpty()) {
            throw new IncorrectInputException();
        }
        return new EventCommand(desc, fromS, toS);
    }

    /**
     * Parses a one-based index from the given string.
     *
     * @param s the string to parse (can contain spaces)
     * @return the one-based index as an int
     * @throws EmptyException if the string is empty/blank
     * @throws IncorrectInputException if not a positive integer
     */
    private static int parseIndexOneBased(String s) {
        final String t = (s == null) ? "" : s.replaceAll("\\s+", "");
        if (t.isEmpty()) {
            throw new EmptyException();
        }
        try {
            int idx = Integer.parseInt(t);
            if (idx <= 0) { // disallow 0 or negatives
                throw new IncorrectInputException();
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new IncorrectInputException();
        }
    }

    // ===== Small utilities =====

    /** case-insensitive indexOf. */
    private static int indexOfIgnoreCase(String haystack, String needle) {
        return indexOfIgnoreCase(haystack, needle, 0);
    }

    /** case-insensitive indexOf, from a given offset. */
    private static int indexOfIgnoreCase(String haystack, String needle, int fromIdx) {
        final String h = haystack.toLowerCase();
        final String n = needle.toLowerCase();
        return h.indexOf(n, Math.max(0, fromIdx));
    }
}
