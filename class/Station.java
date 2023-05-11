import java.util.ArrayList;

public class Station{

    String name;
    ArrayList<Double> coordinates;
    ArrayList<Ligne> lignes;

    //Distance and neighbor it's use for Disjkra 
    Integer distance = Integer.MAX_VALUE;
    ArrayList<Edge> neighbor = new ArrayList<Edge>();

    public Boolean contains(Ligne l){
        return lignes.contains(l);
    }

    public ArrayList<Edge> getNeighbor() {
        return neighbor;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Edge getminiedge(Station s) {
        Integer mini = Integer.MAX_VALUE;
        Edge temp = null;
        for(Edge i : neighbor){
            if(i.containStation(this) && i.containStation(s) && i.getWeight() < mini) {
                mini = i.getWeight();
                temp = i;
            }
        }
        return temp;
    }
    
}