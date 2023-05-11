import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    HashSet<Station> stations;
    HashMap<Station, Edge> anterior;

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
                if(i.getWeight() < mini){
                    mini = i.getWeight();
                    sommet = i.getotherParent(s);
                }
            }
        }
        return sommet;
    }

    private void update_weight(Station s1, Station s2) {
        if(s2.getDistance() > (s1.getDistance() + s1.getminiedge(s2).getWeight())){
            s2.setDistance((s1.getDistance() + s1.getminiedge(s2).getWeight()));
            anterior.put(s2, s1.getminiedge(s2));
        }
    }

    public String stationligne(Ligne l) {
        return l.toString();
    }
}