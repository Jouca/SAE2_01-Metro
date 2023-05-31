import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;

public class StationFinder {
    Graph graphe;
    ArrayList<Station> stations;
    StationFinder() throws IOException, CsvValidationException {
        this.graphe = new Graph();
        this.stations = graphe.getStationsList();
    }

    public void findBestPath(String stationBeginName, String stationArrivalName) {
        ArrayList<Station> beginStations = findStationsByName(stationBeginName);
        ArrayList<Station> arrivalStations = findStationsByName(stationArrivalName);

        if (beginStations.size() == 0 || arrivalStations.size() == 0) {
            System.out.println("Les noms de station ne sont pas correcte.");
            return;
        }

        //Station minDijkstraStationBegin = null;
        Station minDijkstraStationArrival = null;
        Dijkstra minDijkstraPath = null;
        int minDijkstraTime = Integer.MAX_VALUE;

        for (Station beginStation : beginStations) {
            Dijkstra dijkstraStations = new Dijkstra(graphe, beginStation);
            for (Station arrivalStation : arrivalStations) {
                if (dijkstraStations.timeTo(arrivalStation.getID()) < minDijkstraTime) {
                    minDijkstraTime = dijkstraStations.timeTo(arrivalStation.getID());
                    //minDijkstraStationBegin = beginStation;
                    minDijkstraStationArrival = arrivalStation;
                    minDijkstraPath = dijkstraStations;
                }
            }
        }

        System.out.println("\n========= " + minDijkstraStationArrival.getName() + " (ligne " + minDijkstraStationArrival.getLigne().getName() + ") =========");
        System.out.println("Station Accessible ? : " + minDijkstraPath.hasPathTo(minDijkstraStationArrival.getID()));
        System.out.println("Temps: " + minDijkstraPath.timeTo(minDijkstraStationArrival.getID()));
        minDijkstraPath.printSP(minDijkstraStationArrival.getID(), graphe);

        //ArrayList<Station> stationsPath = minDijkstraPath.shortestPathTo(minDijkstraStationArrival, graphe);
        //for (Station station : stationsPath) {
        //   System.out.println(station.getName());
        //}
    }

    private ArrayList<Station> findStationsByName(String stationName) {
        ArrayList<Station> stations = new ArrayList<>();
        for (Station station : this.stations) {
            if (station.getName().equals(stationName)) {
                stations.add(station);
            }
        }
        return stations;
    }
}
