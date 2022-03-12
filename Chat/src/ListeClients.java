
import java.awt.Dimension;
import javax.swing.*;

public class ListeClients extends JPanel{
    private JList<String> liste;
    DefaultListModel<String> clients;

    public ListeClients(){
        clients = new DefaultListModel<String>();
        liste = new JList<String>(clients);
        this.setSize(new Dimension(120,301));
        this.setVisible(true);
        this.add(liste);
    }

    public void addClient(String nom){
        clients.addElement(nom);
    }

    public void removeClient(String nom){
        clients.removeElement(nom);
    }

    public void clearClient(){
        clients.clear();
    }

    public String toString(){
        String res = "%%CLIENTS%%,";
        for(int i = 0; i < clients.size(); i++) {
            res+=clients.get(i)+",";
        }
        return res;
    }
}
