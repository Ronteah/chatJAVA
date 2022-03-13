import java.awt.*;
import javax.swing.*;

public class TestScrollBar{

    private JTextPane txt;
    private JScrollPane scroll;
    
    public TestScrollBar(){

        JFrame frame = new JFrame("Test panel");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible (true);
        frame.setResizable(false);

        //Ajuste la taille de la fenêtre
        frame.setSize(new Dimension (470, 520));
        frame.setLayout(null);

        scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        txt = new JTextPane();
        txt.setBounds(20,20,200,200);
        frame.add(txt);

        scroll.setBounds(20,20,200,200);
        scroll.getViewport().add(txt);
        frame.add(scroll);
    }

    public static void main (String[] args) {
        new TestScrollBar();
    }
}
