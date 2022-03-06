import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable{

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    
    public ClientProcessor(Socket pSock){
        sock = pSock;
    }
    
    //Le traitement lancé dans un thread séparé
    public void run(){
        System.err.println("Lancement du traitement de la connexion cliente");

        //tant que la connexion est active, on traite les demandes
        while(!sock.isClosed()){
            try {
                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());
                
                //On attend la demande du client            
                String reponse = read();              
                if(reponse == "%%CLOSE%%"){
                    System.out.println("Client deconnecte.");
                }else{
                    System.out.println("Message recu : "+reponse);
                }       
                
                String toSend = "";

                //On envoie la réponse au client
                writer.write(toSend);
                writer.flush();
                        
            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }         
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
