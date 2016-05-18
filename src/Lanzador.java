/**
 * Autor: Francisco Menéndez Moya (NIP: 540891)
 *
 * Esta clase es la encargada de lanzar el broker, los servidores y el cliente
 *
 */
public class Lanzador {

    /**
     *
     * @param args  -Primer argumento: tipo de lanzamiento(broker, servidorA, servidor B o cliente)
     *              -Segundo argumento: ip donde van a buscar el servidor RMI
     */
    public static void main(String[] args) {
        if(args.length != 0){
            System.setProperty("java.security.policy", "politica.policy");
            System.setSecurityManager(new SecurityManager());

            if(args[0].equals("-serverA")){
                //Lanzar servidor A

                System.out.println("Lanzando Servidor A");
                System.out.println();

                ServerAImp serverA;

                //Comprobamos si hay IP
                if(args.length < 2){
                    //No IP
                    serverA = new ServerAImp();
                } else{
                    //IP
                    serverA = new ServerAImp(args[1]);
                }
                Thread thread = new Thread(serverA);
                thread.start();
            } else if(args[0].equals("-serverB")){
                //Lanzar servidor B

                System.out.println("Lanzando Servidor B");
                System.out.println();

                ServerBImp serverB;

                //Comprobamos si hay IP
                if(args.length < 2){
                    //No IP
                    serverB = new ServerBImp();
                } else{
                    //IP
                    serverB = new ServerBImp(args[1]);
                }
                Thread thread = new Thread(serverB);
                thread.start();
            } else if(args[0].equals("-broker")){
                //Lanzar broker

                System.out.println("Lanzando broker");
                System.out.println();

                BrokerImp brokerImp;
                //Comprobamos si hay IP
                if(args.length < 2){
                    //No IP
                    brokerImp = new BrokerImp();
                } else{
                    //IP
                    brokerImp = new BrokerImp(args[1]);
                }

                Thread thread = new Thread(brokerImp);
                thread.start();
            } else if(args[0].equals("-cliente")){
                //Lanzar cliente

                System.out.println("Lanzando Cliente");
                System.out.println();

                Cliente cliente;

                //Comprobamos si hay IP
                if(args.length < 2){
                    //No IP
                    cliente = new Cliente("");
                } else{
                    //IP
                    cliente = new Cliente(args[1]);
                }
                Thread thread = new Thread(cliente);
                thread.start();
            } else{
                System.err.println("Error, opcion no reconocida.");
            }
        } else{
            System.err.println("Error, debe haber al menos un parametro.");
        }
    }
}
