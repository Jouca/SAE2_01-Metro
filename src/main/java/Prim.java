import java.util.ArrayList;
import java.util.HashSet;

public class Prim {
    Graph graph;
     ArrayList<Edge> edges;

    Prim(Graph graph) {
        this.graph = graph;
        this.edges = prim();
    }

    public ArrayList<Edge> prim() {
        HashSet<String> selected_nodes = new HashSet<String>();
        ArrayList<Edge> edges = new ArrayList<Edge>();

        //prend une première station
        selected_nodes.add(this.graph.getStationsList().get(0).getName());

        //tant qu'on n'a pas pris chaque station
        
        while(selected_nodes.size() != graph.getStations().size()) {
            Edge nextedge = new Edge(null, null, Integer.MAX_VALUE);
            for(String n : selected_nodes) {
                for(Station s : graph.findStationsByName(n)) {
                    for(Edge e : graph.getNeighbors(s)){

                        if((e.getTimeWeight() < nextedge.getTimeWeight()) && (!selected_nodes.contains(e.getOtherParent(s).getName()))) {
                            nextedge = e;
                        }
                    }
                }
            }
            try {
                selected_nodes.add(nextedge.getArrivalStation().getName());
                selected_nodes.add(nextedge.getDepartureStation().getName());
                edges.add(nextedge);
            }
            catch(Exception e) {
                System.out.println("DOUBLONS DANS Graph.stations");
                System.out.println(selected_nodes.size());
                return edges;
            }
        }
        return edges;
    }

    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    
}