import java.util.*;

public class Dijkstra2 {
    Graph graph;
    ArrayList<Station> selected_nodes;
    ArrayList<Integer> weight;
    ArrayList<Integer> previous;
    Dijkstra2(Graph graph, Station begin) {
        this.graph = graph;
        if(!verifyNoNegativeEdges()){
            System.out.println("Negatif weight");
            return;
        }

        //initialisation, chaque sommet en infini sauf le début
        this.weight = new ArrayList<Integer>();
        this.previous = new ArrayList<Integer>();

        System.out.println(graph.getStationsList().size());

        for(Station s: graph.getStationsList()){
            weight.add(Integer.MAX_VALUE);
            previous.add(null);
        }
        for(Station s: graph.getStationsList()){
            weight.add(s.getID(), Integer.MAX_VALUE);
            previous.add(s.getID(), null);
        }
        weight.add(begin.getID(), 0);

        this.selected_nodes = new ArrayList<Station>();
        while(selected_nodes.size() != this.graph.getStationsList().size()){

            // Recherche du plus petit sommet
            Integer mini = Integer.MAX_VALUE;
            Station sommet = null;
            for(Station s : this.graph.getStationsList()){
                if(weight.get(s.getID()) < mini && !selected_nodes.contains(s)){
                    mini = weight.get(s.getID());
                    sommet = s;
                }
            }
            selected_nodes.add(sommet);

            for(Edge e: sommet.neighbors) {
                Integer distance = weight.get(sommet.getID()) + e.timeWeight;
                if(weight.get(e.getOtherParent(sommet).getID()) > distance){
                    weight.set(e.getOtherParent(sommet).getID(), distance); 
                    previous.set(e.getOtherParent(sommet).getID(), sommet.getID());
                }
            }
        }

    }

    private boolean verifyNoNegativeEdges() {
        for (Station station : this.graph.getStationsList()) {
            for (Edge edge : station.getNeighbors()) {
                if (edge.getTimeWeight() < 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public int timeTo(int v) {
        return weight.get(v);
    }

    public ArrayList<Station> shortestPathTo(Station station) {
        return SP(station, new ArrayList<>());
    }

    private ArrayList<Station> SP(Station station, ArrayList<Station> path) {
        if (!this.selected_nodes.contains(station)) {
            return null;
        }

        Station currentStation = this.graph.getStationByID(station.getID());

        if (this.previous.get(station.getID()) == null) {
            path.add(currentStation);
            return path;
        }
        path = SP(this.graph.getStationByID(this.previous.get(station.getID())), path);
        path.add(currentStation);

        return path;
    }
}