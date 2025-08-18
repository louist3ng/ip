import java.util.Scanner;

public class Jason {
    private static String intro = "Hello, my name is Jason";

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
            } else {
                System.out.println("─".repeat(50));
                System.out.println(userInput);
                System.out.println("─".repeat(50));
            }
        }

        sc.close();
    }
}
