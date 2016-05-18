import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la interfaz que exporta el Broker al servidor RMI
 *
 */

public interface Broker extends Remote {

    /**
     * Este método sirve para ejecutar en los servidores registrados en el broker aquellos métodos
     * con los parámetros que se le pasan por parámetro
     * @param nom_servicio método del servidor a ejecutar
     * @param parametros_Servicio parámetros del método del servidor a ejecutar
     * @return resultado de la ejecución del método con sus parámetros
     * @throws RemoteException
     */
    String ejecutar_servicio(String nom_servicio, ArrayList<String> parametros_Servicio) throws RemoteException;

    /**
     * Este método sirve para registrar un servidor nuevo en el broker
     * @param host_remoto_IP_port nombre, dirección y puerto del servidor que se registra
     * @param nombre_registrado nombre por el que se le identifica
     * @throws RemoteException
     */
    void registrar_servidor(String host_remoto_IP_port, String nombre_registrado) throws RemoteException;

    /**
     * Este método sirve para registrar un servicio en el broker de un servidor existente
     * @param nombre_registrado nombre por el que se identifica al servidor
     * @param nom_servicio nombre del servicio (método) que ofrece el servidor
     * @param lista_param parámetros que requiere dicho servicio que se va a registrar
     * @param tipo_retorno tipo de objeto que devuelve el método (si es que devuelve algo)
     * @throws RemoteException
     */
    void registrar_servicio(String nombre_registrado, String nom_servicio, ArrayList<String> lista_param,
                            String tipo_retorno) throws RemoteException;

    /**
     * Este método obtiene un listado de todos los servicios registrados en el broker en este momento
     * @return lista de métodos registrados
     * @throws RemoteException
     */
    ArrayList<String> listarServicios() throws RemoteException;
}
