import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdderImpl extends UnicastRemoteObject implements Adder {
    // Constructor
    protected AdderImpl() throws RemoteException {
        super();
    }

    // Implementation of the add method
    public int add(int x, int y) throws RemoteException {
        return x + y;
    }
}