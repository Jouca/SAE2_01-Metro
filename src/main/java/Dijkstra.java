import java.util.HashMap;
import java.util.List;

public class Dijkstra {
    HashMap<Station, Edge> anterior = new HashMap<>();
    HashMap<String, Station> stations = new HashMap<>();
    boolean[] marked;
    int[] previous;
    int[] time;
    private boolean verifyNoNegativeEdges() {
        for (Station station : this.stations.values()) {
            for (Edge edge : station.getNeighbors()) {
                if (edge.getTimeWeight() < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    Dijkstra(Graph subwayNetwork, Station begin) {
        int beginStationID = begin.getID();

        // Vérifie si aucun des temps est en négatif dans les arrêtes
        if (!verifyNoNegativeEdges()) {
            System.out.println("Il y a des temps négatifs, il n'est donc pas possible de montrer le véritable chemin le plus court.");
            return;
        }

        // Initialisation des stations visités, le temps et la liste des stations précédentes
        marked = new boolean[subwayNetwork.stationsList.size()];
        time = new int[subwayNetwork.stationsList.size()];
        previous = new int[subwayNetwork.stationsList.size()];
        for (int i = 0; i < subwayNetwork.stationsList.size(); i++) {
            marked[i] = false;
            time[i] = (i == beginStationID) ? 0 : Integer.MAX_VALUE;
            previous[i] = -1;
        }

        boolean allVisitedStations = false;

        // Regarde si toutes les stations ont été vérifié
        while (!allVisitedStations) {
            // We initialize the current path and the current node
            int currentPath = Integer.MAX_VALUE;
            int currentNode = -1;
            for (int i = 0; i < time.length; i++) {
                if (!marked[i] && time[i] < currentPath) {
                    currentNode = i;
                    currentPath = time[currentNode];
                }
            }

            // We add the current node to the list of the visited node
            marked[currentNode] = true;

            // We now look at all the neighbours of the current nodes
            List<Edge> neighbours = subwayNetwork.getNeighbors(currentNode);
            for (Edge directedEdge : neighbours) {
                int distanceAlt = time[currentNode] + directedEdge.getTimeWeight();
                int destinationEdge = directedEdge.getOtherParent(begin).getID();
                if (distanceAlt < time[destinationEdge]) {
                    time[destinationEdge] = distanceAlt;
                    previous[destinationEdge] = currentNode;
                }
            }

            // We check if we have visited all the nodes
            allVisitedStations = true;
            for (boolean isVisited : marked) {
                if (!isVisited) {
                    // At least one node hasn't benn visited yet
                    allVisitedStations = false;
                    break;
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        // If the node was marked, it should be visited
        // if a node is not visited at the end of the algorithm then there is no path
        return marked[v];
    }

    public int timeTo(int v) {
        return time[v];
    }

    public void printSP(int v, Graph graphe) {
        if (!hasPathTo(v)) {
            System.out.println("Aucun chemin avec " + v);
            return;
        }

        Station currentStation = graphe.getStationByID(v);

        if (previous[v] == -1) {
            System.out.println("Voici le chemin le plus court : \n");
            System.out.print(currentStation.getName() + " (ligne " + currentStation.getLigne().getName() + ")\n");
            return;
        }
        printSP(previous[v], graphe);
        System.out.print(" ==> " + currentStation.getName() + " (ligne " + currentStation.getLigne().getName() + ")\n");
    }

    private Station nearestNeighbor(Station s) {
        Integer minWeight = Integer.MAX_VALUE;
        Station sommet = null;

        for (Edge i : s.getNeighbors()) {
            if (i.getTimeWeight() < minWeight) {
                minWeight = i.getTimeWeight();
                sommet = i.getOtherParent(s);
            }
        }

        return sommet;
    }

    private void update_weight(Station s1, Station s2) {
        Edge miniedge = getminiedge(s1, s2);
        if(s2.getDistance() > (s1.getDistance() + miniedge.getTimeWeight())){
            s2.setDistance((s1.getDistance() + miniedge.getTimeWeight()));
            anterior.put(s2, miniedge);
        }
    }

    public Edge getminiedge(Station s1, Station s2) {
        Integer mini = Integer.MAX_VALUE;
        Edge temp = null;
        for(Edge i : s1.getNeighbors()) {
            /*
            if (i.containStation(s1) && i.containStation(s2) && i.getLigne().equals(anterior.get(s1).getLigne())){
                temp = i;
                break;
            }
            else if(i.containStation(s1) && i.containStation(s2) && i.getWeight() < mini) {
                mini = i.getWeight();
                temp = i;
            }*/
            if (i.containStation(s1) && i.containStation(s2)) {
                mini = i.getTimeWeight();
                temp = i;
            }
        }
        return temp;
    }
}
