import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Serveur {
    
    private static int port;
    private String host = "127.0.0.1";
    private ServerSocket serveur = null;
    private boolean isRunning = true;
    private ListeClients listeClients;
    private static boolean scannerEtat = true;

    /**
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Entrez un port svp : ");
        try (Scanner sc = new Scanner(System.in)) {            
            while(scannerEtat){
                try{
                    port = sc.nextInt();
                }catch(Exception exc){
                    System.out.println("Erreur: Entrez un entier ! Fermeture...");
                    Thread.sleep(2000);
                    return;
                }
            

                if(Integer.toString(port).matches("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")){
                    scannerEtat = false;
                }else{
                    System.out.println("Le port est invalide ! Reessayez : ");
                }
            }
        }
        Serveur serv = new Serveur ();
        serv.open();
    }
    

    /**
     * 
     * Constructeurs
     */

    /**
     * 
     * Constructeur sans paramètre, utile lorsqu'on ne possède pas l'ip ni le port du serveur
     */ 
    public Serveur(){
        try {
            serveur = new ServerSocket(port, 100, InetAddress.getByName(host));
            System.out.println("Serveur : " + host + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * 
     * Second constructeur utile lorsqu'on possède déjà l'ip et le port du serveur
     * @param pHost
     * @param pPort
     */
    public Serveur(String pHost, int pPort){
        host = pHost;
        port = pPort;
        try {
            serveur = new ServerSocket(port, 100, InetAddress.getByName(host));
            System.out.println("Serveur : " + host + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /**
     * 
     * Méthodes de classe
     */

    
    /**
     * 
     * Fonction permettant d'ouvrir le serveur
     */
    public void open(){
        listeClients = new ListeClients();
        
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(isRunning == true){
                    try {
                        //On attend une connexion d'un client
                        Socket client = serveur.accept();
                        
                        //Une fois reçue, on la traite dans un thread séparé
                        System.out.println("Un client a rejoint.");                  
                        Thread t = new Thread(new ClientProcessor(client, listeClients, isRunning));
                        t.start();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                try {
                    serveur.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    serveur = null;
                }
            }
        });
        
        t.start();
    }
    

    /**
     * 
     * Fonction permettant la fermeture du serveur
     */
    public void close(){
        isRunning = false;
        System.out.println("Fermeture serveur");   
    }   
}
