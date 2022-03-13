
import javax.swing.*;

public class ListaClients extends DefaultListModel<String>{
    public void addClient(String nom){
        this.addElement(nom);
    }

    public void removeClient(String nom){
        this.removeElement(nom);
    }

    public void clearClient(){
        this.clear();
    }

    public String toString(){
        String res = "%%CLIENTS%%,";
        for(int i = 0; i < this.size(); i++) {
            res+=this.get(i)+",";
        }
        return res;
    }
}
