import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        Graph graphe = new Graph();
        ArrayList<Station> stations = graphe.getStationsList();

        // IDFM:44371 - Place D'italie
        // IDFM:42587 - Chatelet

        Dijkstra dijkstraStations = new Dijkstra(graphe, graphe.stations.get("IDFM:44371"));

        for (Station station : stations) {
            System.out.println("\n========="+station.getName()+" (ligne "+station.getLigne().getName()+") =========");
            System.out.println("hasPathTo: " + dijkstraStations.hasPathTo(station.getID()));
            System.out.println("Temps: " + dijkstraStations.timeTo(station.getID()));
            dijkstraStations.printSP(station.getID(), graphe);
        }
    }
}