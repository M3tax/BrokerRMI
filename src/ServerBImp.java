import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la implementación de la interfaz del servidor
 *
 */

public class ServerBImp implements ServerB, Runnable{

    private static String host;         //IP donde se encuentra el servidor RMI
    private ArrayList<String> libros;   //Listado de libros

    /**
     * Constructor
     */
    public ServerBImp() {
        host = "";
        libros = new ArrayList<>();
    }

    /**
     * Constructor
     * @param host2 ip del servidor RMI
     */
    public ServerBImp(String host2) {
        host = host2;
        libros = new ArrayList<>();
    }

    /**
     * Ejecución del servidor B, el cual busca el registro RMI, obtiene el broker, y registra su servidor y servicios
     * en dicho broker.
     */
    public void run(){
        try{

            //Obtenemos la direccionIP de la maquina en la que estamos
            InetAddress direccionIP = Inet4Address.getLocalHost();

            //Obtenemos nuestros metodos disponibles
            ServerB interfaz = new ServerBImp();
            ServerB interfazRemota = (ServerB) UnicastRemoteObject.exportObject(interfaz,0);

            Registry registro;

            //Obtenemos los nombres de los objetos registrados en RMI
            if(host.equals("")){
                //No hay IP
                registro = LocateRegistry.getRegistry();
            } else{
                registro = LocateRegistry.getRegistry(host);
            }

            System.out.println("Creando y lanzando el servidor B con ip: " + direccionIP);
            System.out.println();

            //Nos registramos con el registro RMI
            registro.rebind(direccionIP + "/ServidorB", interfazRemota);

            //Buscamos el broker
            Broker broker = (Broker) registro.lookup("Broker");
            System.out.println("Broker encontrado, registrando servicios...");
            System.out.println();

            //Registramos el servidor
            broker.registrar_servidor(direccionIP + "/ServidorB","ServidorB");
            ArrayList<String> parametros = new ArrayList<>();

            //Registramos los metodos
            broker.registrar_servicio("ServidorB","lista_libros",parametros,"String");
            parametros.add("titulo");
            broker.registrar_servicio("ServidorB","introducir_libro",parametros,"String");

            System.out.println("Servicios registrados, quedando a la espera de invocaciones......");
        } catch(RemoteException e){
            System.err.println("Error en conexion remota, causa probable: No está lanzado el servidor RMI");
        } catch(NotBoundException e){
            System.err.println("Error al hacer lookup, causa probable: Broker no encontrado");
        } catch (UnknownHostException e) {
            System.err.println("Error al obtener la ip de la maquina");
        }
    }

    /**
     * Método que introduce un nuevo libro en el servidor
     * @param titulo nombre del libro
     * @throws RemoteException
     */
    public void introducir_libro(String titulo) throws RemoteException {
        libros.add(titulo);
    }

    /**
     * Método que devuelve un listado de todos los libros almacenados en el servidor
     * @return nombre de los titulos de los libros almacenados en el servidor
     * @throws RemoteException
     */
    public String lista_libros() throws RemoteException {
        return libros.toString();
    }
}
