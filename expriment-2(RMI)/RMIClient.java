import java.rmi.Naming;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Look up the remote object in the RMI registry
            Adder adder = (Adder) Naming.lookup("rmi://localhost/AdderService");

            // Get input from the user
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter first number: ");
            int x = scanner.nextInt();

            System.out.print("Enter second number: ");
            int y = scanner.nextInt();

            // Call the remote method and display the result
            int result = adder.add(x, y);
            System.out.println("The sum is: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
