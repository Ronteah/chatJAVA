import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Sender extends Thread{

    private final BufferedReader in; 
    private final PrintWriter out;
    private JTextArea messageBox;
    private JButton bouton;
    private String message;

    public Sender(BufferedReader in, PrintWriter out, JTextArea msg, JButton btn){
        this.in = in;
        this.out = out;
        messageBox = msg;
        bouton = btn;

        bouton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                message = messageBox.getText();
                System.out.println(message);
                try {
                    envoyerMessage(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        start();
    }

    public void envoyerMessage(String msg) throws IOException{
        msg = in.readLine();
        if(msg != null){
            out.write(msg);
            out.flush();
        }
    }

    public void run(){
        while(true){}
    }
}
