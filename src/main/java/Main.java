import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        Graph graphe = new Graph();

        // IDFM:44371 - Place D'italie
        // IDFM:42587 - Chatelet
        for (Edge edge : graphe.stations.get("IDFM:42587").getNeighbor()) {
            System.out.println(edge.getDepartureStation() + "\n" + edge.getArrivalStation() + "\nLigne : " + edge.ligne + "\nTemps : " + edge.getTimeWeight() + "\n\n");
        }


        System.out.println(graphe.Dijkstra(graphe.stations.get("IDFM:42587"), graphe.stations.get("IDFM:44371")));
    }
}