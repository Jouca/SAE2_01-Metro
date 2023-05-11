import java.util.ArrayList;

public class Edge {

    ArrayList<Station> parents;
    Ligne ligne;
    int weight;

    
    public int getWeight() {
        return weight;
    }


    public ArrayList<Station> getParents() {
        return parents;
    }

    public Station getotherParent(Station station) {
        return (parents.get(0) == station) ? parents.get(1) : parents.get(0);
    }

    public Boolean containStation(Station station) {
        return (parents.contains(station));
    }
}