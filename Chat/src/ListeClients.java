import java.util.List;
import java.util.ArrayList;

public class ListeClients{
    private List<Client> listeClients;

    /**
     * 
     * Constructeur
     */
    public ListeClients(){
        listeClients = new ArrayList<Client>();
    }



    /**
     * 
     * Méthodes de classe
     */

    /**
     * Ajoute un client à la liste des clients connectés
     * @param c
     */
    public void addClient(Client c){
        listeClients.add(c);
    }

    /**
     * Enlève un client à la liste des clients connectés
     * @param c
     */
    public void removeClient(Client c){
        listeClients.remove(c);
    }

    /**
     * 
     * Réinitialise la liste des clients connectés
     */
    public void clearClients(){
        listeClients.clear();
    }

    /**
     * 
     * Retourne la liste des clients connectés
     * @return List<Client>
     */
    public List<Client> getListe(){
        return listeClients;
    }


    /**
     * 
     * Renvoie la liste des clients connectés sous forme de texte
     * @return String
     */
    public String toString(){
        String res = "%%CLIENTS%%,";
        for(int i = 0; i < listeClients.size(); i++) {
            res+=listeClients.get(i).getName()+",";
        }
        return res;
    }
}
