import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Serveur {
    
    private static int port;
    private String host = "127.0.0.1";
    private ServerSocket serveur = null;
    private boolean isRunning = true;

    public static void main(String[] args) {
        port = Integer.parseInt(args[0]);

        Serveur serv = new Serveur ();
        serv.open();
    }
    
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
    
    
    //On lance notre serveur
    public void open(){
        
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(isRunning == true){
                    try {
                        //On attend une connexion d'un client
                        Socket client = serveur.accept();
                        
                        //Une fois reçue, on la traite dans un thread séparé
                        System.out.println("Un client a rejoint.");                  
                        Thread t = new Thread(new ClientProcessor(client));
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
    
    public void close(){
        isRunning = false;
        System.out.println("Fermeture serveur");   
    }   
}
