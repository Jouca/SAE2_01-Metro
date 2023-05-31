import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        // IDFM:44371 - Place D'italie
        // IDFM:42587 - Chatelet

        StationFinder stationFinder = new StationFinder();
        stationFinder.findBestPath("Ch\u00e2telet", "Villejuif L\u00e9o Lagrange");
    }
}