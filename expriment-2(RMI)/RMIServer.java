import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // Start the RMI registry on port 1099
            LocateRegistry.createRegistry(1099);

            // Create an instance of the Adder implementation
            AdderImpl adder = new AdderImpl();

            // Bind the remote object to the RMI registry
            Naming.rebind("rmi://localhost/AdderService", adder);

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

