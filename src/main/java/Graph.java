import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    HashSet<Station> stations = new HashSet<>();
    HashMap<Station, Edge> anterior = new HashMap<>();;

    public void addstation(Station station) {
        stations.add(station);
    }

    public ArrayList<Edge> Dijkstra(Station begin, Station end){
        begin.setDistance(0);
        HashSet<Station> nodes = new HashSet<>(stations);
        while(!nodes.isEmpty()) {
            Station s1 = minneighbor(nodes);
            for(Edge e : s1.getNeighbor()){
                update_weight(s1, e.getotherParent(s1));
            }
        }

        ArrayList<Edge> a = new ArrayList<>();
        while(end != begin) {
            a.add(0, anterior.get(end));
            end = anterior.get(end).getotherParent(end);
        }

        return a;
    }

    private Station minneighbor(HashSet<Station> set) {
        Integer mini = Integer.MAX_VALUE;
        Station sommet = null ;

        for(Station s: set) {
            for(Edge i : s.getNeighbor()){
                if(i.getWeight() < mini ){
                    mini = i.getWeight();
                    sommet = i.getotherParent(s);
                }
            }
        }
        return sommet;
    }

    private void update_weight(Station s1, Station s2) {
        Edge miniedge = getminiedge(s1, s2);
        if(s2.getDistance() > (s1.getDistance() + miniedge.getWeight())){
            s2.setDistance((s1.getDistance() + miniedge.getWeight()));
            anterior.put(s2, miniedge);
        }
    }

    public Edge getminiedge(Station s1, Station s2) {
        Integer mini = Integer.MAX_VALUE;
        Edge temp = null;
        for(Edge i : s1.getNeighbor()) {
            if (i.containStation(s1) && i.containStation(s2) && i.getLigne().equals(anterior.get(s1).getLigne())){
                temp = i;
                break;
            }
            else if(i.containStation(s1) && i.containStation(s2) && i.getWeight() < mini) {
                mini = i.getWeight();
                temp = i;
            }
        }
        return temp;
    }

    public String stationligne(Ligne l) {
        return l.toString();
    }

    public String correspondance() {
        HashSet<Ligne> cor = new HashSet<Ligne>();

        for(Station s : stations) {
            for(Ligne i : s.getLignes()){
                cor.add(i);
            }
        }

        return cor.toString();        
    }

    public String correspondanceligne(Ligne l) {
        HashSet<Ligne> cor = new HashSet<Ligne>();

        for(Station s : l.getStations()) {
            if(stations.contains(s)){
                for(Ligne i : s.getLignes()){
                    cor.add(i);
                }
            }
        }

        return cor.toString();        
    }

}