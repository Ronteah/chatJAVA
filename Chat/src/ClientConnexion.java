import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import java.awt.event.*;

public class ClientConnexion implements Runnable{

    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    
    private String name;   

    JButton sendBtn;
    JButton deconnectBtn;
    JTextArea message;
    JTextPane chat;
    StyledDocument doc;

    private boolean estConnecte = true;
    
    public ClientConnexion(String host, int port, String name, JTextArea msg, JButton btn, JButton btnDeco, JTextPane chat){
        this.name = name;
        message = msg;
        sendBtn = btn;
        deconnectBtn = btnDeco;
        this.chat = chat;
        doc = chat.getStyledDocument();

        try {
            connexion = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    

    public void run(){

        try {
            doc.insertString(doc.getLength(), "> System : Vous avez rejoint le chat.\n", null);
        } catch (BadLocationException e2) {
            e2.printStackTrace();
        }

        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                //Envoi
                String msg = "> "+name+" : "+message.getText();
                writer.write(msg);
                writer.flush();  

                System.out.println("Message : \"" + msg + "\" envoye par " + name);

            }
        });
        
        deconnectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){                        
                
                if(estConnecte){
                    System.out.println("Deconnexion de "+name);

                    writer.write("%%CLOSE%%");
                    writer.flush();

                    estConnecte = false;
                }else{
                    estConnecte = true;
                }

            }
        });

        while(true){
            try {
                Thread.currentThread();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {               
                writer = new PrintWriter(connexion.getOutputStream(), true);
                reader = new BufferedInputStream(connexion.getInputStream());              
                                
                //On attend la réponse
                String reponse = read();
                System.out.println("\t * " + name + " : Reponse recue " + reponse);
                doc.insertString(doc.getLength(), reponse + "\n", null);
                reponse = "";
                
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            
            try {
                Thread.currentThread();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    

    //Méthode pour lire les réponses du serveur
    private String read() throws IOException{      
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);      
        return response;
    }   
}