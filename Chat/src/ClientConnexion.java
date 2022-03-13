import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import javax.swing.text.*;

import java.awt.event.*;
import java.awt.*;

public class ClientConnexion implements Runnable{

    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    
    private String name;   

    private JButton sendBtn;
    private JButton deconnectBtn;
    private JTextArea message;
    private JTextPane chat;
    private StyledDocument doc;

    private ListeClients listeClients;
    private DefaultListModel<String> modeleClients;

    private String[] reponseClients;

    private boolean estConnecte = true;

    private Client client;
    

    /**
     * 
     * Constructeur
     */

    /**
     * 
     * Constructeur prenant en paramètre des objets de la Frame afin de récupérer et modifier leur contenu
     * @param host
     * @param port
     * @param name
     * @param msg
     * @param btn
     * @param btnDeco
     * @param chat
     * @param clients
     * @param modele
     */
    public ClientConnexion(String host, int port, String name, JTextArea msg, JButton btn, JButton btnDeco, JTextPane chat, ListeClients clients, DefaultListModel<String> modele){
        this.name = name;
        message = msg;
        sendBtn = btn;
        deconnectBtn = btnDeco;
        this.chat = chat;
        doc = chat.getStyledDocument();
        listeClients = clients;
        modeleClients = modele;
        client = new Client(name);

        try {
            connexion = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println();
        } catch (IOException e) {
            System.out.println("Serveur inaccessible !");
        }
    }    
    


    /**
     * 
     * Méthodes de classe
     */

    /**
     * 
     * Fonction permettant de lancer le Thread communicant avec le serveur et récupérant des informations
     */
    public void run(){

        try {
            doc.insertString(doc.getLength(), ">>> System : Vous avez rejoint le chat.\n", null);
        } catch (BadLocationException e2) {
            e2.printStackTrace();
        }

        //Message d'erreur lorsque le serveur est hors ligne
        if(connexion == null){
            JOptionPane.showMessageDialog(null, 
                            "Serveur inaccessible !",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);
        }

        try {
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());    
        } catch (IOException e2) {
            System.out.println("Serveur inaccessible !");
        }

        //Envoi les informations du client au serveur
        writer.write("%%NAME%%:"+name+":"+client.getColor());
        writer.flush(); 


        //Lorsqu'on appuie sur le bouton Envoyé
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
                //Envoi
                if(!message.getText().equals("")){
                    String msg = "> "+name+" : "+message.getText();

                    //Envoie le message et la couleur à laquelle il devra être affiché
                    writer.write(msg + "::" + client.getColor());
                    writer.flush();  

                    System.out.println("Message : \"" + msg + "\" envoye par " + name);
                    message.setText("");
                }

            }
        });
        
        //Lorsqu'on appuie sur le bouton Déconnexion
        deconnectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){                        
                
                if(estConnecte){
                    System.out.println("Deconnexion de "+name);

                    //Envoie l'inforation au serveur que ce client se déconnecte
                    writer.write("%%CLOSE%%");
                    writer.flush();

                    //Réinitialise la zone de chat et les clients connectés
                    chat.setText(""); 
                    listeClients.clearClients();
                    modeleClients.clear();

                    estConnecte = false;
                }else{
                    estConnecte = true;
                }

            }
        });

        while(estConnecte){
            try {
                Thread.currentThread();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println();
            }
            try {                            

                //On attend la réponse
                String reponse = read();

                System.out.println("\t * " + name + " : Reponse recue " + reponse);


                //Si la réponse du serveur reçu concerne les clients
                if(reponse.startsWith("%%CLIENTS%%")){
                    reponseClients = reponse.split(",");
                    
                    listeClients.clearClients();
                    modeleClients.clear();

                    //Transmission la liste des clients connectés à ce client depuis le serveur
                    for(int i=1; i<reponseClients.length; i++){
                        System.out.println(reponseClients[i]);
                        listeClients.addClient(new Client(reponseClients[i]));
                        modeleClients.addElement(reponseClients[i]);
                    }

                    System.out.println("Post reponseClients : "+listeClients.toString());
                    reponseClients = null;

                //Si le serveur envoie un message comme quoi il ferme
                }else if(reponse.startsWith("%%SERVCLOSED%%")){

                    //Affiche un message d'erreur comme quoi le serveur n'est plus en ligne
                    JOptionPane.showMessageDialog(null, 
                            "Serveur déconnecté !",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);

                //Pour tous les autres types de réponses
                }else{
                    reponseClients = reponse.split("::");

                    //Récupère la couleur dans laquelle le message devra être écrit
                    Style style = chat.addStyle("Couleur texte", null);
                    StyleConstants.setForeground(style, traduireCouleur(reponseClients[1]));

                    //Ecrit le message dans la zone de chat avec la bonne couleur
                    doc.insertString(doc.getLength(), reponseClients[0] + "\n", style); 
                }

                reponse = "";
                
            }catch (IOException e1){
                System.out.println("Serveur inaccessible !");
                JOptionPane.showMessageDialog(null, 
                            "Serveur hors service !",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);
            }catch (BadLocationException e2){
                System.out.println();
            }catch (StringIndexOutOfBoundsException e3){
                System.out.println("");
            }
            
            try {
                Thread.currentThread();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Serveur inaccessible !");
            }
        }
    }

    /**
     * 
     * Traduit la couleur envoyée par le serveur d'un objet String à un objet Color
     * @param s
     * @return
     */
    private Color traduireCouleur(String s){
        switch (s) {
            case "Color.GREEN":
                return Color.GREEN;
            case "Color.BLUE":
                return Color.BLUE;
            case "Color.CYAN":
                return Color.CYAN;
            case "Color.LIGHT_GRAY":
                return Color.LIGHT_GRAY;
            case "Color.RED":
                return Color.RED;
            case "Color.MAGENTA":
                return Color.MAGENTA;
            default:
                return null;
        }
    }
    

    //Méthode pour lire les réponses du serveur
    private String read() throws IOException, SocketException{      
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);      
        return response;
    }   
}