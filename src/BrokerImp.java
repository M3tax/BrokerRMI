import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la implementación de los métodos de la interfaz Broker
 *
 */

public class BrokerImp implements Broker, Runnable{

    private static String host;
    private ArrayList<Pair<String,String>> servidoresRegistrados;       //Array de parejas nombreServer, @server
    private ArrayList<Pair<String,String>> servidorServicio;            //Pareja nombreServer, nombreServicio
    private ArrayList<Pair<String,ArrayList<String>>> servicioParams;   //Pareja nombreServicio, listadoParametros
    private ArrayList<Pair<String,String>> servicioRetorno;             //Pareja nombreServicio, tipoRetorno

    /**
     * Constructor
     */
    public BrokerImp(){
        host = "";
        inicializarArrays();
    }

    /**
     * Constructor
     * @param host2 ip del broker
     */
    public BrokerImp(String host2){
        host = host2;
        inicializarArrays();
    }

    /**
     * Método para inicializar las estructuras de datos que se utilizan en la clase
     */
    private void inicializarArrays(){
        servidoresRegistrados = new ArrayList<>();
        servidorServicio = new ArrayList<>();
        servicioParams = new ArrayList<>();
        servicioRetorno = new ArrayList<>();
    }

    /**
     * Ejecución del broker, el cual exporta su interfaz al servidor RMI y se queda esperando a que le
     * invoquen algún método
     */
    public void run(){
        try{

            //Obtenemos nuestros metodos disponibles
            Broker interfaz = new BrokerImp();
            Broker interfazRemota = (Broker) UnicastRemoteObject.exportObject(interfaz,0);

            Registry registro;

            //Obtenemos los nombres de los objetos registrados en RMI
            if(host.equals("")){
                //No hay IP
                registro = LocateRegistry.getRegistry();
            } else{
                registro = LocateRegistry.getRegistry(host);
            }

            System.out.println("Creando y lanzando el Broker");
            System.out.println();

            //Paso 2: Nos registramos con el registro RMI
            registro.rebind("Broker", interfazRemota);
        } catch(RemoteException e){
            System.err.println("Error en conexion remota, causa probable: No está lanzado el servidor RMI");
        }
    }

    /**
     * Este método sirve para ejecutar en los servidores registrados en el broker aquellos métodos
     * con los parámetros que se le pasan por parámetro
     * @param nom_servicio método del servidor a ejecutar
     * @param parametros_Servicio parámetros del método del servidor a ejecutar
     * @return resultado de la ejecución del método con sus parámetros
     * @throws RemoteException
     */
    public String ejecutar_servicio(String nom_servicio, ArrayList<String> parametros_Servicio) throws RemoteException{

        System.out.println("Ejecutando el servicio " + nom_servicio +
                " con parametros " + parametros_Servicio.toString());

        for(Pair<String,String> servidoresYservicios : servidorServicio){
            String nombreServidorAlmacenado = servidoresYservicios.getLeft();
            String nombreServicioAlmacenado = servidoresYservicios.getRight();
            String direccionServidor = obtenerDireccion(nombreServidorAlmacenado);

            if(nombreServicioAlmacenado.equalsIgnoreCase(nom_servicio)){
                //Obtener servidor
                Registry registro;

                try{
                    //Obtenemos los nombres de los objetos registrados en RMI
                    if(host.equals("")){
                        //No hay IP
                        registro = LocateRegistry.getRegistry();
                    } else{
                        registro = LocateRegistry.getRegistry(host);
                    }

                    if(registro != null){
                        if(nombreServidorAlmacenado.equalsIgnoreCase("ServidorA")){
                            //Obtener servidor A
                            try {
                                ServerA servidorA = (ServerA) registro.lookup(direccionServidor);
                                Class claseServidorA = servidorA.getClass();
                                Method[] metodosClaseServidorA = claseServidorA.getDeclaredMethods();

                                int numMetodo = -1;

                                //De todos los métodos del sevidor A buscamos el que nos han solicitado
                                for(int i = 0; i < metodosClaseServidorA.length; i++){
                                    if(metodosClaseServidorA[i].getName().equalsIgnoreCase(nombreServicioAlmacenado)){
                                        numMetodo = i;
                                    }
                                }

                                //Con el número obtenido previamente obtenemos el método deseado
                                switch(numMetodo){
                                    case 3:     //dar_fecha
                                        return servidorA.dar_fecha();
                                    case 4:     //dar_hora
                                        return servidorA.dar_hora();
                                    default:
                                        System.err.println("Error, metodo no encontrado");
                                        return "Error al ejecutar el metodo (no encontrado) "
                                                + nom_servicio + " en la maquina " + nombreServidorAlmacenado;
                                }

                            } catch (RemoteException | NotBoundException e) {
                                System.err.println("Error al obtener el servidor A en ejecutar_servicio");
                                return "Error, excepcion encontrada";
                            }
                        } else{
                            //Obtener servidor B
                            try {
                                ServerB servidorB = (ServerB) registro.lookup(direccionServidor);
                                Class claseServidorB = servidorB.getClass();
                                Method[] metodosClaseServidorB = claseServidorB.getDeclaredMethods();

                                int numMetodo = -1;

                                //De todos los métodos del sevidor A buscamos el que nos han solicitado
                                for(int i = 0; i < metodosClaseServidorB.length; i++){
                                    if(metodosClaseServidorB[i].getName().equalsIgnoreCase(nombreServicioAlmacenado)){
                                        numMetodo = i;
                                    }
                                }

                                //Con el número obtenido previamente obtenemos el método deseado
                                switch(numMetodo){
                                    case 3:     //introducir_libro(String titulo)
                                        for(String libro: parametros_Servicio){
                                            servidorB.introducir_libro((libro));
                                        }
                                        return "Exito";
                                    case 4:     //listar_libros()
                                        return servidorB.lista_libros();
                                    default:
                                        System.err.println("Error, metodo no encontrado");
                                        return "Error al ejecutar el metodo (no encontrado) "
                                                + nom_servicio + " en la maquina " + nombreServidorAlmacenado;
                                }

                            } catch (RemoteException | NotBoundException e) {
                                System.err.println("Error al obtener el servidor A en ejecutar_servicio");
                                return "Error, excepcion encontrada";
                            }
                        }
                    } else{
                        System.err.println("Registro nulo");
                    }

                } catch(RemoteException e){
                    System.err.println("Error al obtener el registro en ejecutar_servicio");
                    return "Error, excepcion encontrada";
                }
            }
        }
        return "";
    }

    /**
     * Este método sirve para registrar un servidor nuevo en el broker
     * @param host_remoto_IP_port nombre, dirección y puerto del servidor que se registra
     * @param nombre_registrado nombre por el que se le identifica
     * @throws RemoteException
     */
    public void registrar_servidor(String host_remoto_IP_port, String nombre_registrado) throws RemoteException{
        System.out.println("Registrando el servidor " + host_remoto_IP_port + " con nombre " + nombre_registrado);
        servidoresRegistrados.add(new Pair<>(nombre_registrado,host_remoto_IP_port));
    }

    /**
     * Este método sirve para registrar un servicio en el broker de un servidor existente
     * @param nombre_registrado nombre por el que se identifica al servidor
     * @param nom_servicio nombre del servicio (método) que ofrece el servidor
     * @param lista_param parámetros que requiere dicho servicio que se va a registrar
     * @param tipo_retorno tipo de objeto que devuelve el método (si es que devuelve algo)
     * @throws RemoteException
     */
    public void registrar_servicio(String nombre_registrado, String nom_servicio, ArrayList<String> lista_param,
                                   String tipo_retorno) throws RemoteException{
        System.out.println("Registrando servicio " + nom_servicio + " con parametros " + lista_param.toString() +
                            " con retorno de tipo " + tipo_retorno + " del servidor " + nombre_registrado);
        servidorServicio.add(new Pair<>(nombre_registrado,nom_servicio));
        servicioParams.add(new Pair<>(nom_servicio,lista_param));
        servicioRetorno.add(new Pair<>(nom_servicio,tipo_retorno));
    }

    /**
     * Este método obtiene un listado de todos los servicios registrados en el broker en este momento
     * @return lista de métodos registrados
     * @throws RemoteException
     */
    public ArrayList<String> listarServicios() throws RemoteException{

        System.out.println("Listando servicios al cliente");
        ArrayList<String> devolver = new ArrayList<>();

        for(int i = 0; i < servidorServicio.size(); i++){
            String nombreServicio = servidorServicio.get(i).getRight();
            ArrayList<String> parametros = servicioParams.get(i).getRight();
            String tipoRetorno = servicioRetorno.get(i).getRight();

            //De la lista de los parametros almacenados, los parseamos en un único String
            String parametrosParsed ="";
            for(String parametro: parametros){
                parametrosParsed = parametrosParsed + parametro + ",";
            }
            if(!parametrosParsed.equals("")){
                parametrosParsed = parametrosParsed.substring(0,parametrosParsed.length() - 1);
            }

            //Sintaxis de devolución de los servicios ofrecidos
            String servicio = "public " + tipoRetorno + " " + nombreServicio + "(" + parametrosParsed + ")";
            devolver.add(servicio);
        }

        return devolver;
    }

    /**
     * Método que sirve para obtener la dirección de un servidor cuyo nombre es nombreServidor
     * @param nombreServidor nombre del servidor
     * @return direccion del servidor con nombre nombreServidor
     */
    private String obtenerDireccion(String nombreServidor){
        String direccion = "";

        for(Pair<String,String> direccionNombre: servidoresRegistrados){
            if(direccionNombre.getLeft().equalsIgnoreCase(nombreServidor)){
                direccion = direccionNombre.getRight();
                break;
            }
        }

        return direccion;

    }
}
