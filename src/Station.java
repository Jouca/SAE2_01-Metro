import java.util.ArrayList;

public class Station{

    String name;
    ArrayList<Double> coordinates;
    ArrayList<Ligne> lignes;

    public Boolean contains(Ligne l){
        return lignes.contains(l);
    }
}
