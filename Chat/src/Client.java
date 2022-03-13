import java.util.Random;
import java.io.PrintWriter;

public class Client{
    private final String name;
    private PrintWriter writer;
    private String couleur;


    /**
     * 
     * Constructeurs
     */

    /**
     * 
     * Constructeur à simple paramètre, utile pour le côté client de l'application
     * @param name
     */
    public Client(String name){
        this.name = name;
        couleur = generateColor();
    }

    /**
     * 
     * Constructeur à 2 paramètres, utile pour le côté serveur de l'application
     * Le writer étant utilisé pour envoyer des messages à tous les clients
     * @param name
     * @param writer
     */
    public Client(String name, PrintWriter writer){
        this.name = name;
        this.writer = writer;
        couleur = generateColor();
    }



    /**
     * 
     * Méthodes de classe
     */

    /**
     * Retourne le nom du client
     * @return String
     */
    public String getName(){
        return name;
    }

    /**
     * Retourne le writer du client
     * @return PrintWriter
     */
    public PrintWriter getWriter(){
        return writer;
    }

    /**
     * Retourne la couleur du client
     * @return String
     */
    public String getColor(){
        return couleur;
    }

    /**
     * Met à jour la couleur du client
     * @param couleur
     */
    public void setColor(String couleur){
        this.couleur = couleur;
    }

    /**
     * Retourne une couleur prise aléatoirement dans un tableau de couleurs
     * @return String
     */
    private String generateColor(){
        String[] couleur = {
            "Color.GREEN",
            "Color.BLUE",
            "Color.CYAN",
            "Color.LIGHT_GRAY",
            "Color.RED",
            "Color.MAGENTA"
        };
        Random rand = new Random();
        return couleur[rand.nextInt(5)];
    }
}
