import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa el lanzador del registro RMI
 *
 */
public class LanzarRMI {
    public static void main(String[] args) {
        try {
            System.setSecurityManager(new RMISecurityManager());
            LocateRegistry.createRegistry(1099);
            System.out.println("Server Ready");
            while (true) {
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
