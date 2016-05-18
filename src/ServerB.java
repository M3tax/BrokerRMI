import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la interfaz que exporta el Servidor B al registro RMI
 *
 */

public interface ServerB extends Remote{

    /**
     * Método que introduce un nuevo libro en el servidor
     * @param titulo nombre del libro
     * @throws RemoteException
     */
    void introducir_libro(String titulo) throws RemoteException;

    /**
     * Método que devuelve un listado de todos los libros almacenados en el servidor
     * @return nombre de los titulos de los libros almacenados en el servidor
     * @throws RemoteException
     */
    String lista_libros() throws RemoteException;
}
