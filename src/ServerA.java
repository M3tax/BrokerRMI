import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la interfaz que exporta el Servidor A al registro RMI
 *
 */

public interface ServerA extends Remote{

    /**
     * Metodo que obtiene la fecha actual en formato dia/mes/año
     * @return fecha actual
     * @throws RemoteException
     */
    String dar_fecha() throws RemoteException;

    /**
     * Metodo que obtiene la hora actual en formato hora:minuto:segundo
     * @return hora actual
     * @throws RemoteException
     */
    String dar_hora() throws RemoteException;

}
