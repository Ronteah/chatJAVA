import java.io.PrintWriter;

public class ClientServeur{
    private final String name;
    private PrintWriter writer;

    public ClientServeur(String name, PrintWriter writer){
        this.name = name;
        this.writer = writer;
    }

    public String getName(){
        return name;
    }

    public PrintWriter getWriter(){
        return writer;
    }
}
