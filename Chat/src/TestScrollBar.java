import java.awt.*;
import javax.swing.*;

public class TestScrollBar{
    
    public TestScrollBar(){

        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible (true);
        frame.setResizable(false);

        //Ajuste la taille de la fenêtre
        frame.setSize(new Dimension (470, 520));
        frame.setLayout(null);


        ListeClients clients = new ListeClients();
        clients.addClient("Louis");
        frame.add(clients);
    }

    public static void main (String[] args) {
        new TestScrollBar();
    }
}
