import java.net.*;
import java.io.*;
import java.util.*;

public class Serveur {  

    public void deconnectClient(){
                
    }
    
    public static void main(String[] args) throws UnknownHostException {
        final ServerSocket serverSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        
        System.out.println("Starting server on port 5000 local address is " + InetAddress.getLocalHost().getHostAddress());
        try{
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable(){
                String msg;
    
                @Override
                public void run(){
                    while(true){
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            sender.start();
    
            Thread receiver = new Thread(new Runnable(){
                String msg;
    
                @Override
                public void run(){
                    try{
                        System.out.println("Client connecté");
                        msg = in.readLine();
                        while(msg!=null){
                            System.out.println("Client : "+msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client déconnecté");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();

        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
