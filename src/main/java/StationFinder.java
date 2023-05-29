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
        Station beginStation = findStationByName(stationBeginName);
        Station arrivalStation = findStationByName(stationArrivalName);

        if (beginStation == null || arrivalStation == null) {
            System.out.println("Les noms de station ne sont pas correcte.");
            return;
        }

        Dijkstra dijkstraStations = new Dijkstra(graphe, beginStation);

        for (Station station : stations) {
            if (station.getID() == arrivalStation.getID()) {
                System.out.println("\n========= " + station.getName() + " (ligne " + station.getLigne().getName() + ") =========");
                System.out.println("hasPathTo: " + dijkstraStations.hasPathTo(station.getID()));
                System.out.println("Temps: " + dijkstraStations.timeTo(station.getID()));
                dijkstraStations.printSP(station.getID(), graphe);
            }
        }
    }

    private Station findStationByName(String stationName) {
        for (Station station : stations) {
            if (station.getName().equals(stationName)) {
                return station;
            }
        }
        return null;
    }
}
