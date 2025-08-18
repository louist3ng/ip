import java.util.Scanner;

public class Jason {
    private static String intro = "Hello, my name is Jason";
    private static String[] items = new String[100];
    private static int itemPointer = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String userInput = "";

        System.out.println("─".repeat(50));
        System.out.println(intro);

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
                    System.out.println((i+1) + ". " + items[i]);
                }
                System.out.println("─".repeat(50));

            } else {
                System.out.println("─".repeat(50));
                System.out.println("added: " + userInput);
                System.out.println("─".repeat(50));
                items[itemPointer] = userInput;
                itemPointer++;
            }
        }
        sc.close();
    }
}
