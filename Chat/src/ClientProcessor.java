import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable{

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private ListeClients listeClients;
    private String[] name;
    private String nom;
    private String couleur;
    private boolean isRunning;

    private Client client;
        

    /**
     * 
     * Constructeur
     */

    /**
     * Constructeur récupérant les informations depuis le serveur
     * @param pSock
     * @param listeClients
     * @param isRunning
     */
    public ClientProcessor(Socket pSock, ListeClients listeClients, boolean isRunning){
        sock = pSock;
        this.listeClients = listeClients;
        this.isRunning = isRunning;
    }
    

    /**
     * 
     * Méthodes de classe
     */

    /**
     * 
     * Fonction permettant de faire la communication entre le serveur et le/les client(s)
     */
    public void run(){
        System.err.println("Lancement du traitement de la connexion cliente");

        //Tant que la connexion est active et que le serveur est en ligne, on traite les demandes
        while(!sock.isClosed() || isRunning){
            try {
                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());
                
                //On attend la demande du client            
                String reponse = read();              
                
                //Si la demande concerne la déconnexion du client
                if(reponse.equals("%%CLOSE%%")){
                    System.out.println(nom+" s'est deconnecte.");
                    listeClients.removeClient(client);

                    //Met à jour et renvoie de la liste des clients connectés à tous les autres clients
                    for(Client c : listeClients.getListe()){
                        c.getWriter().write(listeClients.toString());
                        c.getWriter().flush();
                    }

                    sock.close();

                //Si la demande concerne la connexion du client au serveur
                }else if(reponse.startsWith("%%NAME%%")){

                    //Récupère le nom et la couleur du client
                    name = reponse.split(":");
                    nom = name[1];
                    couleur = name[2];
                    client = new Client(nom,writer);
                    client.setColor(couleur);

                    //Ajoute le client à la liste des clients connectés
                    listeClients.addClient(client);

                    //Envoie de la liste des clients connectés                    
                    for(Client c : listeClients.getListe()){
                        c.getWriter().write(listeClients.toString());
                        c.getWriter().flush();
                    }

                //Pour toutes les autres demandes
                }else{
                    System.out.println("Message recu : "+reponse);
                    System.out.println();

                    //On envoie la réponse à tous les clients
                    for(Client c : listeClients.getListe()){
                        c.getWriter().write(reponse);
                        c.getWriter().flush();
                    }
                }       
                
                reponse = "";

                        
            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                listeClients.removeClient(client);

                //Envoie de la liste des clients connectés                    
                for(Client c : listeClients.getListe()){
                    c.getWriter().write(listeClients.toString());
                    c.getWriter().flush();
                }

                break;
            } catch (IOException e) {
                System.out.println("Serveur hors service !");
            }         
        }
        //Envoie de la liste des clients connectés                    
        for(Client c : listeClients.getListe()){
            c.getWriter().write("%%SERVCLOSED%%");
            c.getWriter().flush();
        }
    }
    
    //La méthode que nous utilisons pour lire les réponses
    private String read() throws IOException{      
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
