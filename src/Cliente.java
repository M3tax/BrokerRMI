import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase representa la ejecución de un cliente que solicita servicios a un Broker
 *
 */

public class Cliente implements Runnable{

    private String host;

    /**
     * Constructor
     * @param host IP donde se conecta y busca el cliente
     */
    public Cliente(String host) {
        this.host = host;
    }

    /**
     * Ejecución del cliente, el cual busca el registro RMI, obtiene el broker, le pide el menú de los servicios
     * que ofrece, le pregunta al usuario que método con que parámetros quiere ejecutar y envía dicha petición
     * al broker, obteniendo finalmente un resultado que muestra por pantalla
     */
    public void run(){
        try{
            Registry reg;

            //Comprobamos si hay IP para registrarnos
            if(host.equals("")){
                reg = LocateRegistry.getRegistry();
            }
            else{
                reg = LocateRegistry.getRegistry(host);
            }

            //Buscamos el broker
            Broker broker = (Broker) reg.lookup("Broker");
            System.out.println("Broker encontrado, solicitando menú...");
            System.out.println();

            //Mostramos el menú de operaciones del broker
            ArrayList<String> menu = broker.listarServicios();
            if(menu.size() > 0){
                Scanner lecturaTeclado = new Scanner(System.in);
                for(String opcionMenu: menu){
                    System.out.println(opcionMenu);
                }

                //Obtenemos la operacion y metodos a invocar
                System.out.println();
                System.out.println("¿Qué operación desea ejecutar? (Sólo nombre del método):");
                String operacion = lecturaTeclado.nextLine();
                System.out.println("¿Con qué parámetros? (Separados por espacios):");
                String parametros = lecturaTeclado.nextLine();

                //Obtenemos un arraylist de los parametros
                String[] parametrosSinParsear = parametros.split(" ");
                ArrayList<String> parametrosParseados = new ArrayList<>();
                Collections.addAll(parametrosParseados, parametrosSinParsear);

                //Invocamos la operación del broker deseada
                String resultado = broker.ejecutar_servicio(operacion,parametrosParseados);

                //Mostramos el resultado de la operación por pantalla
                System.out.println();
                System.out.println(resultado);
            } else{
                System.out.println("No hay servicios registrados en el broker, finalizando ejecución.");
            }

        } catch(RemoteException e){
            System.err.println("Error en conexion remota, causa probable: No está lanzado el servidor RMI");
        } catch(NotBoundException e){
            System.err.println("Error al hacer lookup, causa probable: Broker no encontrado");
        }
    }
}
