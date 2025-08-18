import java.util.Scanner;

public class Jason {
    private static String intro = "Hello, my name is Jason";
    private static Task[] items = new Task[100];
    private static int itemPointer = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String userInput = "";

        System.out.println("─".repeat(50));
        System.out.println(intro);

        //ensures that chatbot does not close until "bye" string is inputted by user
        while(!userInput.equalsIgnoreCase("bye")){ 
            userInput = sc.nextLine();

            if(userInput.equalsIgnoreCase("bye")){
                System.out.println("─".repeat(50));
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("─".repeat(50));
                break;

            } else if(userInput.toLowerCase().equalsIgnoreCase("list")) {  
                System.out.println("─".repeat(50));
                for (int i = 0; i < itemPointer; i++) {
                    System.out.println((i+1) + ". " + items[i].getDescription());
                }
                System.out.println("─".repeat(50));

            } else if(userInput.toLowerCase().startsWith("mark")) {
                String target = userInput.substring(5);
                int itemPosition = Integer.parseInt(target) - 1;

                System.out.println("─".repeat(50));
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + items[itemPosition].mark().getDescription());
                System.out.println("─".repeat(50));

            } else if(userInput.toLowerCase().startsWith("unmark")) {
                String target = userInput.substring(7);
                int itemPosition = Integer.parseInt(target) - 1;

                System.out.println("─".repeat(50));
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + items[itemPosition].unmark().getDescription());
                System.out.println("─".repeat(50));

            } else if (userInput.toLowerCase().startsWith("todo")) {
                String target = userInput.substring(    5);
                Todo todoTask = new Todo(target);

                System.out.println("─".repeat(50));
                System.out.println("Got it. I've added this task:");
                System.out.println(todoTask.getDescription());
                System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                System.out.println("─".repeat(50));
                items[itemPointer] = todoTask;
                itemPointer++;

            } else if (userInput.toLowerCase().startsWith("event")) {
                String parts = userInput.substring(6).trim(); // after "event "
                int fromIdx = parts.toLowerCase().indexOf("/from");
                int toIdx = parts.toLowerCase().indexOf("/to", Math.max(fromIdx, 0) + 5);
                String description = parts.substring(0, fromIdx).trim();
                String from = parts.substring(fromIdx + 5, toIdx).trim(); // 5 = len("/from")
                String to = parts.substring(toIdx + 3).trim();            // 3 = len("/to")

                Event eventTask = new Event(description, from, to);

              

                System.out.println("─".repeat(50));
                System.out.println("Got it. I've added this task:");
                System.out.println(eventTask.getDescription());
                System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                System.out.println("─".repeat(50));
                items[itemPointer] = eventTask;
                itemPointer++;

            } else if (userInput.toLowerCase().startsWith("deadline")) {
                String[] parts = userInput.substring(9).split("/by", 2);
                String description = parts[0].trim();
                String by = parts[1].trim();
                Task todoTask = new Deadline(description, by);
    
                System.out.println("─".repeat(50));
                System.out.println("Got it. I've added this task:");
                System.out.println(todoTask.getDescription());
                System.out.printf("Now you have %d tasks in the list.\n", itemPointer + 1);
                System.out.println("─".repeat(50));

                items[itemPointer] = todoTask;
                itemPointer++;
            }


        }
        sc.close();
    }
}
