import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Adder extends Remote {
    // Method to perform addition
    public int add(int x, int y) throws RemoteException;
}