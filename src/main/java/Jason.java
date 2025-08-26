import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Jason {
    private static String intro = "Hello, my name is Jason";
    private static ArrayList<Task> items = new ArrayList<>();
    private static Storage storage = new Storage("data/jason.txt");

    private static void intro() {
        System.out.println("─".repeat(50));
        System.out.println(intro);
        System.out.println("─".repeat(50));
    }

    private static void saveNow() {
        try {
            storage.save(items); 
        } catch (IOException e) {
            System.err.println("[WARN] Failed to save tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String userInput = "";

        try {
            ArrayList<Task> loaded = storage.load();
            items.addAll(loaded);
            if (!loaded.isEmpty()) {
                System.out.printf("[Loaded %d tasks from disk]%n", items.size());
            }
        } catch (IOException e) {
            System.err.println("[WARN] Could not load tasks: " + e.getMessage());
        }

        intro();
        // ensures that chatbot does not close until "bye" string is inputted by user
        while (!userInput.equalsIgnoreCase("bye")) {
            // System.out.println("Enter: "); include this for future use
            userInput = sc.nextLine();

            try {
                if (userInput.equalsIgnoreCase("bye")) {
                    System.out.println("─".repeat(50));
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println("─".repeat(50));
                    break;

                } else if (userInput.toLowerCase().equalsIgnoreCase("list")) {
                    System.out.println("─".repeat(50));
                    for (int i = 0; i < items.size(); i++) {
                        System.out.println((i + 1) + ". " + items.get(i).getDescription());
                    }
                    System.out.println("─".repeat(50));

                } else if (userInput.toLowerCase().startsWith("mark")) {
                    if (userInput.equalsIgnoreCase("mark")) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("mark ")) {
                        throw new IncorrectInputException();
                    }

                    String target = userInput.substring(4)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= items.size() || itemPosition < 0) {
                        throw new OobIndexException();
                    }

                    System.out.println("─".repeat(50));
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + items.get(itemPosition).mark().getDescription());
                    System.out.println("─".repeat(50));

                } else if (userInput.toLowerCase().startsWith("unmark")) {
                    if (userInput.equalsIgnoreCase("unmark")) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("unmark ")) {
                        throw new IncorrectInputException(); // catches "unmarks"
                    }

                    String target = userInput.substring(6)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= items.size() || itemPosition < 0) {
                        throw new OobIndexException();
                    }

                    System.out.println("─".repeat(50));
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + items.get(itemPosition).unmark().getDescription());
                    System.out.println("─".repeat(50));

                } else if (userInput.toLowerCase().startsWith("todo")) {
                    if (userInput.length() == 4) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("todo ")) {
                        throw new IncorrectInputException(); // catches "todos"
                    }

                    String target = userInput.substring(5);
                    Todo todoTask = new Todo(target);

                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + todoTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", items.size() + 1);
                    System.out.println("─".repeat(50));
                    items.add(todoTask);
                    saveNow();

                } else if (userInput.toLowerCase().startsWith("event")) {
                    if (userInput.length() == 5) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("event ")) {
                        throw new IncorrectInputException(); // catches "unmarks"
                    }

                    String parts = userInput.substring(6).trim(); // after "event "
                    int fromIdx = parts.toLowerCase().indexOf("/from");
                    int toIdx = parts.toLowerCase().indexOf("/to", Math.max(fromIdx, 0) + 5);
                    String description = parts.substring(0, fromIdx).trim();
                    String fromStr = parts.substring(fromIdx + 5, toIdx).trim(); // 5 = len("/from")
                    String toStr = parts.substring(toIdx + 3).trim(); // 3 = len("/to")

                    LocalDateTime from = DateTimeUtil.parseDayMonthYearWithTime(fromStr, DateTimeUtil.PREFER_DMY);
                    LocalDateTime to = LocalDateTime.of(from.toLocalDate(), DateTimeUtil.parseTimeHm(toStr));

                    if (to.isBefore(from)) {
                        to = to.plusDays(1);
                    }

                    Event eventTask = new Event(description, from, to);
                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + eventTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", items.size());
                    System.out.println("─".repeat(50));
                    items.add(eventTask);
                    saveNow();

                } else if (userInput.toLowerCase().startsWith("deadline")) {
                    if (userInput.length() == 8) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("deadline ")) {
                        throw new IncorrectInputException(); // catches "unmarks"
                    }

                    String[] parts = userInput.substring(9).split("/by", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    LocalDateTime byDateTime = DateTimeUtil.parseIsoDateOrDateTime(by);
                    Task deadlineTask = new Deadline(description, byDateTime);

                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + deadlineTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", items.size() + 1);
                    System.out.println("─".repeat(50));

                    items.add(deadlineTask);
                    saveNow();

                } else if (userInput.toLowerCase().startsWith("delete")) {
                    if (userInput.length() == 6) {
                        throw new EmptyException();
                    }

                    if (!userInput.toLowerCase().startsWith("delete ")) {
                        throw new IncorrectInputException(); // catches "deletes"
                    }

                    String target = userInput.substring(6)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= items.size() || itemPosition < 0) {
                        throw new OobIndexException();
                    }

                    System.out.println("─".repeat(50));
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + items.get(itemPosition).getDescription());
                    items.remove(itemPosition);
                    System.out.printf("Now you have %d tasks in the list.\n", items.size());
                    System.out.println("─".repeat(50));
                    saveNow();

                } else {
                    throw new IncorrectInputException();
                }

            } catch (EmptyException | OobIndexException | IncorrectInputException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println("☹ OOPS!!! The task number should be an integer.");
            }
        }
        sc.close();
    }
}
