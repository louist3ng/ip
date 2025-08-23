import java.util.ArrayList;
import java.util.Scanner;


public class Jason {
    private static String intro = "Hello, my name is Jason";
    private static ArrayList<Task> items = new ArrayList<>();
    private static int itemPointer = 0;

    private static void intro() {
        System.out.println("─".repeat(50));
        System.out.println(intro);
        System.out.println("─".repeat(50));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String userInput = "";

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
                    for (int i = 0; i < itemPointer; i++) {
                        System.out.println((i + 1) + ". " + items.get(i).getDescription());
                    }
                    System.out.println("─".repeat(50));

                } else if (userInput.toLowerCase().startsWith("mark")) {
                    if (userInput.length() == 4) {
                        throw new EmptyException();
                    }

                    String target = userInput.substring(4)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= itemPointer || itemPosition < 0) {
                        throw new OobIndexException();
                    }

                    System.out.println("─".repeat(50));
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + items.get(itemPosition).mark().getDescription());
                    System.out.println("─".repeat(50));

                } else if (userInput.toLowerCase().startsWith("unmark")) {
                    if (userInput.length() == 5) {
                        throw new EmptyException();
                    }

                    String target = userInput.substring(6)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= itemPointer || itemPosition < 0) {
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

                    String target = userInput.substring(5);
                    Todo todoTask = new Todo(target);

                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(todoTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                    System.out.println("─".repeat(50));
                    items.add(itemPointer, todoTask);
                    itemPointer++;

                } else if (userInput.toLowerCase().startsWith("event")) {
                    if (userInput.length() == 5) {
                        throw new EmptyException();
                    }

                    String parts = userInput.substring(6).trim(); // after "event "
                    int fromIdx = parts.toLowerCase().indexOf("/from");
                    int toIdx = parts.toLowerCase().indexOf("/to", Math.max(fromIdx, 0) + 5);
                    String description = parts.substring(0, fromIdx).trim();
                    String from = parts.substring(fromIdx + 5, toIdx).trim(); // 5 = len("/from")
                    String to = parts.substring(toIdx + 3).trim(); // 3 = len("/to")

                    Event eventTask = new Event(description, from, to);

                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(eventTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                    System.out.println("─".repeat(50));
                    items.add(itemPointer, eventTask);
                    itemPointer++;

                } else if (userInput.toLowerCase().startsWith("deadline")) {
                    if (userInput.length() == 8) {
                        throw new EmptyException();
                    }

                    String[] parts = userInput.substring(9).split("/by", 2);
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    Task deadlineTask = new Deadline(description, by);

                    System.out.println("─".repeat(50));
                    System.out.println("Got it. I've added this task:");
                    System.out.println(deadlineTask.getDescription());
                    System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                    System.out.println("─".repeat(50));

                    items.add(itemPointer, deadlineTask);
                    itemPointer++;
                } else if (userInput.toLowerCase().startsWith("delete")) {
                    if (userInput.length() == 6) {
                        throw new EmptyException();
                    }

                    String target = userInput.substring(6)
                            .replaceAll("\\s+", "");
                    int itemPosition = Integer.parseInt(target) - 1;

                    if (itemPosition >= itemPointer || itemPosition < 0) {
                        throw new OobIndexException();
                    }

                    System.out.println("─".repeat(50));
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + items.get(itemPosition).getDescription());
                    items.remove(itemPosition);
                    itemPointer--;
                    System.out.printf("Now you have %d tasks in the list.\n", itemPointer);
                    System.out.println("─".repeat(50));

                } else {
                    throw new IncorrectInputException();
                }
            } catch (EmptyException e) {
                System.out.println(e);
            } catch (OobIndexException e) {
                System.out.println(e);
            } catch (IncorrectInputException e) {
                System.out.println(e);
            }

        }
        sc.close();
    }
}
