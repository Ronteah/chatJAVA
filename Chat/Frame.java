import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Frame extends JPanel{
    private JLabel nomLabel;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JTextField port;
    private JButton connexionBtn;
    private JLabel connectesLabel;
    private JLabel discussionLabel;
    private JLabel messageLabel;
    private JTextArea listeConnectes;
    private JTextPane chatBox;
    private JTextArea messageBox;
    private JButton envoyerBtn;
    private JTextField nom;
    private JTextField ipBox;
    private boolean estConnecte = false;

    public Frame() {

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
                            fonctionDeconnexion();
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
        
        connectesLabel = new JLabel ("Connectes");
        connectesLabel.setVisible(false);

        discussionLabel = new JLabel ("Discussion");
        discussionLabel.setVisible(false);

        messageLabel = new JLabel ("Message");
        messageLabel.setVisible(false);

        listeConnectes = new JTextArea (5, 5);
        listeConnectes.setEditable(false);
        listeConnectes.setVisible(false);
        
        chatBox = new JTextPane();
        
        chatBox.setEditable(false);
        chatBox.setVisible(false);

        messageBox = new JTextArea (5, 5);
        messageBox.setVisible(false);

        envoyerBtn = new JButton ("Envoyer");
        envoyerBtn.setVisible(false);
        
        nom = new JTextField (5);
        ipBox = new JTextField (5);

        //Ajuste la taille de la fenêtre
        setPreferredSize (new Dimension (454, 475));
        setLayout (null);

        //Ajout des composants
        add (nomLabel);
        add (ipLabel);
        add (portLabel);
        add (ipBox);
        add (connexionBtn);
        add (connectesLabel);
        add (discussionLabel);
        add (messageLabel);
        add (listeConnectes);
        add (chatBox);
        add (messageBox);
        add (envoyerBtn);
        add (nom);
        add (port);

        //Position et taille des composants
        nomLabel.setBounds (25, 30, 100, 25);
        ipLabel.setBounds (25, 80, 100, 25);
        portLabel.setBounds (250, 80, 100, 25);
        ipBox.setBounds (70, 80, 130, 25);
        connexionBtn.setBounds (280, 30, 150, 25);
        connectesLabel.setBounds (50, 130, 100, 25);
        discussionLabel.setBounds (260, 130, 100, 25);
        messageLabel.setBounds (180, 330, 100, 25);
        listeConnectes.setBounds (25, 155, 130, 301);
        chatBox.setBounds (175, 155, 255, 170);
        messageBox.setBounds (175, 355, 255, 60);
        envoyerBtn.setBounds (175, 430, 255, 25);
        nom.setBounds (70, 30, 130, 25);
        port.setBounds (300, 80, 130, 25);


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

    public void testActiveButton(){
        boolean res = !nom.getText().trim().isEmpty() && !ipBox.getText().trim().isEmpty() && !port.getText().trim().isEmpty();
        connexionBtn.setEnabled(res);
    }

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

        //deconnectClient();
    }

    public void fonctionDeconnexion(){
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

        connectClient();
    }

    private void connectClient() {
        Thread t = new Thread(new ClientConnexion(ipBox.getText(), Integer.parseInt(port.getText()), nom.getText(), messageBox, envoyerBtn, connexionBtn));
        t.start();
    }

    public boolean testPort(){
        return port.getText().matches("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
    }

    public boolean testIP(){
        return ipBox.getText().matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }   

    public String getMessage(){
        return messageBox.getText();
    }

    public static void main (String[] args) {
        JFrame frame = new JFrame ("Frame");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new Frame());
        frame.pack();
        frame.setVisible (true);
        frame.setResizable(false);
    }
}
