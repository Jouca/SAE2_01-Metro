import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;

public class StationFinder {
    Graph graphe;
    StationFinder() throws IOException, CsvValidationException {
        this.graphe = new Graph();
    }

    private ArrayList<Station> findStationsByName(String stationName) {
        ArrayList<Station> stations = new ArrayList<>();
        for (Station station : graphe.getStationsList()) {
            if (station.getName().equals(stationName)) {
                stations.add(station);
            }
        }
        return stations;
    }

    public ArrayList<Station> findBestPath(ArrayList<String> stations){
        ArrayList<Station> path = new ArrayList<Station>();

        for(int i=0; i < stations.size()-1; i++){
            ArrayList<Station> beginStations =  findStationsByName(stations.get(i));
            ArrayList<Station> arrivalStations = findStationsByName(stations.get(i+1));

            if (beginStations.size() == 0 || arrivalStations.size() == 0) {
                return null;
            }
    
            Station minDijkstraStationArrival = null;
            Dijkstra minDijkstraPath = null;
            int minDijkstraTime = Integer.MAX_VALUE;
    
            for (Station beginStation : beginStations) {
                Dijkstra dijkstraStations = new Dijkstra(graphe, beginStation);
                for (Station arrivalStation : arrivalStations) {
                    if (dijkstraStations.timeTo(arrivalStation.getID()) < minDijkstraTime) {
                        minDijkstraTime = dijkstraStations.timeTo(arrivalStation.getID());
                        minDijkstraStationArrival = arrivalStation;
                        minDijkstraPath = dijkstraStations;
                    }
                }
            }
            for(Station s : minDijkstraPath.shortestPathTo(minDijkstraStationArrival, graphe)){
                path.add(s);
            }
        }

        return path;
    }

    public void printfindBestPath(ArrayList<String> stations){
        ArrayList<Station> path = this.findBestPath(stations);

        String etape = "";
        for(int i =1; i<stations.size() -1; i++){
            etape+= " " +stations.get(i);
        }
        System.out.println("\n========= "+ stations.get(0) + " vers " + stations.get(stations.size() -1) + " en passant par" + etape + " =========");
        for(Station s : path){
            System.out.print(" ==> " + s.getName() + " (ligne " + s.getLigne().getName() + ")\n");
        }
    }
}
