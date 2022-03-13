import java.awt.*;
import java.awt.event.*;
import java.net.ConnectException;

import javax.swing.*;
import javax.swing.event.*;

public class Frame{
    private JLabel nomLabel;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JTextField port;
    private JButton connexionBtn;
    private JLabel connectesLabel;
    private JLabel discussionLabel;
    private JLabel messageLabel;
    private JList<String> listeConnectes;
    private DefaultListModel<String> listeDesClients;
    private ListeClients listeClients;
    private JTextPane chatBox;
    private JTextArea messageBox;
    private JButton envoyerBtn;
    private JTextField nom;
    private JTextField ipBox;
    private boolean estConnecte = false;

    private JScrollPane scroll;
    private JScrollPane scrollMsg;
    

    /**
     * 
     * Constructeur
     */

    public Frame() {

        JFrame frame = new JFrame("Frame");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible (true);
        frame.setResizable(false);

        //Ajuste la taille de la fenêtre
        frame.setSize(new Dimension (470, 520));
        frame.setLayout(null);

        //Creation composants
        nomLabel = new JLabel ("Nom");
        ipLabel = new JLabel ("IP");
        portLabel = new JLabel ("Port");
        port = new JTextField (5);

        connexionBtn = new JButton ("Connexion");
        connexionBtn.setEnabled(false);
        connexionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(testIP()){
                    if(testPort()){
                        if(estConnecte){
                            fonctionConnexion();
                        }else{
                            try {
                                fonctionDeconnexion();
                            } catch (ConnectException e1) {
                                System.out.println("Impossible de se connecter au serveur !");
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, 
                            "Port invalide !",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, 
                            "IP invalide !",
                            "Erreur",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        connectesLabel = new JLabel ("Clients");
        connectesLabel.setVisible(false);

        discussionLabel = new JLabel ("Discussion");
        discussionLabel.setVisible(false);

        messageLabel = new JLabel ("Message");      
        messageLabel.setVisible(false);



        listeClients = new ListeClients();

        listeDesClients = new DefaultListModel<String>();

        listeConnectes = new JList<String>(listeDesClients);
        listeConnectes.setVisible(false);





        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        chatBox = new JTextPane();
        chatBox.setEditable(false);
        chatBox.setVisible(false);


        

        scrollMsg = new JScrollPane();
        scrollMsg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        messageBox = new JTextArea (5, 5);
        messageBox.setVisible(false);
        messageBox.setMaximumSize(new Dimension(255,60));



        envoyerBtn = new JButton ("Envoyer");
        envoyerBtn.setVisible(false);
        
        nom = new JTextField (5);
        ipBox = new JTextField (5);

        //Ajout des composants
        frame.add(nomLabel);
        frame.add(ipLabel);
        frame.add(portLabel);
        frame.add(ipBox);
        frame.add(connexionBtn);
        frame.add(connectesLabel);
        frame.add(discussionLabel);
        frame.add(messageLabel);
        frame.add(listeConnectes);
        frame.add(chatBox);
        frame.add(messageBox);
        frame.add(envoyerBtn);
        frame.add(nom);
        frame.add(port);

        //Position et taille des composants
        nomLabel.setBounds (25, 30, 100, 25);
        ipLabel.setBounds (25, 80, 100, 25);
        portLabel.setBounds (250, 80, 100, 25);
        ipBox.setBounds (70, 80, 130, 25);
        connexionBtn.setBounds (280, 30, 150, 25);
        connectesLabel.setBounds (45, 130, 100, 25);
        discussionLabel.setBounds (125, 130, 100, 25);
        messageLabel.setBounds (125, 330, 100, 25);
        listeConnectes.setBounds (15, 155, 100, 301);
        chatBox.setBounds (125, 155, 305, 170);
        messageBox.setBounds (125, 355, 305, 60);
        envoyerBtn.setBounds (125, 430, 305, 25);
        nom.setBounds (70, 30, 130, 25);
        port.setBounds (300, 80, 130, 25);

        
        scroll.setBounds(125, 155, 305, 170);
        scroll.getViewport().add(chatBox);
        scroll.setVisible(false);
        frame.add(scroll);

        scrollMsg.setBounds(125, 355, 305, 60);
        scrollMsg.getViewport().add(messageBox);
        scrollMsg.setVisible(false);
        frame.add(scrollMsg);

        nom.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void removeUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void insertUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e){
                testActiveButton();
            }
        });

        ipBox.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void removeUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void insertUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e){
                testActiveButton();
            }
        });

        port.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void removeUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void insertUpdate(DocumentEvent e){
                testActiveButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e){
                testActiveButton();
            }
        });
    }



    /**
     * 
     * Méthodes de classe
     */


    /**
     * 
     * Permet de déverrouiller le bouton de connexion lorsque tous les champs sont valides
     */
    public void testActiveButton(){
        boolean res = !nom.getText().trim().isEmpty() && !ipBox.getText().trim().isEmpty() && !port.getText().trim().isEmpty();
        connexionBtn.setEnabled(res);
    }

    /**
     * 
     * Met à jour l'interface de l'application lorsque le client n'est pas connecté
     */
    public void fonctionConnexion(){
        estConnecte = false;
        connexionBtn.setText("Connexion");
        nom.setEnabled(true);
        ipBox.setEnabled(true);
        port.setEnabled(true);

        connectesLabel.setVisible(false);

        discussionLabel.setVisible(false);

        messageLabel.setVisible(false);

        listeConnectes.setVisible(false);
        
        chatBox.setVisible(false);

        messageBox.setVisible(false);

        envoyerBtn.setVisible(false);

        scroll.setVisible(false);
        scrollMsg.setVisible(false);
        
        listeClients.clearClients();
        listeDesClients.clear();
    }

    /**
     * 
     * Met à jour l'interface de l'application lorsque le client est connecté à un serveur
     * @throws ConnectException
     */
    public void fonctionDeconnexion() throws ConnectException{
        estConnecte = true;
        connexionBtn.setText("Deconnexion");
        nom.setEnabled(false);
        ipBox.setEnabled(false);
        port.setEnabled(false);

        connectesLabel.setVisible(true);

        discussionLabel.setVisible(true);

        messageLabel.setVisible(true);

        listeConnectes.setVisible(true);
        
        chatBox.setVisible(true);

        messageBox.setVisible(true);

        envoyerBtn.setVisible(true);

        scroll.setVisible(true);
        scrollMsg.setVisible(true);

        connectClient();
    }

    /**
     * 
     * Connecte le client au serveur et transmet tous les objets utiles afin de pouvoir mettre à jour l'interface
     * @throws ConnectException
     */
    private void connectClient() throws ConnectException {
        
        //Lance un nouveau thread
        Thread t = new Thread(new ClientConnexion(ipBox.getText(), Integer.parseInt(port.getText()), nom.getText(), messageBox, envoyerBtn, connexionBtn, chatBox, listeClients, listeDesClients));
        t.start();
    }

    /**
     * 
     * Teste si le port entré par le client est valide ou non
     * @return boolean
     */
    public boolean testPort(){
        return port.getText().matches("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
    }


    /**
     * 
     * Teste si l'IP entrée par le client est valide ou non
     * @return boolean
     */
    public boolean testIP(){
        return ipBox.getText().matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }   

    /**
     * 
     * Main lançant ainsi la nouvelle fenêtre
     * @param args
     */
    public static void main (String[] args) {
        new Frame();
    }
}
