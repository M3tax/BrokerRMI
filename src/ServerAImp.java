import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la implementación del servidor A
 *
 */

public class ServerAImp implements ServerA, Runnable{

    private static String host;        //IP donde se encuentra el servidor RMI

    /**
     * Constructor
     */
    public ServerAImp(){ host = ""; }

    /**
     * Constructor
     * @param host2 ip donde se encuentra el servidor RMI
     */
    public ServerAImp(String host2) { host = host2; }

    /**
     * Ejecución del servidor A, el cual obtiene el registro RMI, obtiene el broker y registra el servidor y sus
     * servicios en el mismo
     */
    public void run(){
        try{

            //Obtenemos la direccionIP de la maquina en la que estamos
            InetAddress address = Inet4Address.getLocalHost();
            String direccionIP = address.getHostAddress();

            //Obtenemos nuestros metodos disponibles
            ServerA interfaz = new ServerAImp();
            ServerA interfazRemota = (ServerA) UnicastRemoteObject.exportObject(interfaz,0);

            Registry registro;

            //Obtenemos los nombres de los objetos registrados en RMI
            if(host.equals("")){
                //No hay IP
                registro = LocateRegistry.getRegistry();
            } else{
                registro = LocateRegistry.getRegistry(host);
            }

            System.out.println("Creando y lanzando el servidor A con ip: " + direccionIP);
            System.out.println();

            //Nos registramos con el registro RMI
            registro.rebind(direccionIP + "/ServidorA", interfazRemota);

            //Buscamos el broker
            Broker broker = (Broker) registro.lookup("Broker");
            System.out.println("Broker encontrado, registrando servicios...");
            System.out.println();

            //Registramos el servidor
            broker.registrar_servidor(direccionIP + "/ServidorA","ServidorA");
            ArrayList<String> parametros = new ArrayList<>();

            //Registramos los metodos
            broker.registrar_servicio("ServidorA","dar_fecha",parametros,"String");
            broker.registrar_servicio("ServidorA","dar_hora",parametros,"String");

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
     * Metodo que obtiene la fecha actual en formato dia/mes/año
     * @return fecha actual
     * @throws RemoteException
     */
    public String dar_fecha() throws RemoteException {
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();

        return formato.format(fecha);
    }

    /**
     * Metodo que obtiene la hora actual en formato hora:minuto:segundo
     * @return hora actual
     * @throws RemoteException
     */
    public String dar_hora() throws RemoteException {
        DateFormat formato = new SimpleDateFormat("HH:mm:ss");
        Date fecha = new Date();

        return formato.format(fecha);
    }
}
