import java.util.ArrayList;


public class Ligne {

    String type;
    String name;
    ArrayList<Station> stations;
    String terminus;

    public Boolean contains(Station s) {
        return stations.contains(s);
    }
}