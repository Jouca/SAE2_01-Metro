import java.util.ArrayList;

public class Edge {

    ArrayList<Station> parents = new ArrayList<>();
    Ligne ligne;
    int weight;

    Edge(Station from_station, Station to_station, int time) {
        this.parents.add(from_station);
        this.parents.add(to_station);
        this.weight = time;
    }


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

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    @Override
    public String toString() {
        return "Parents=" + this.parents + ", Lines=" + this.ligne + ", Weight=" + this.weight;
    }
}