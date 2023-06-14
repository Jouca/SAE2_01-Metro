import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        // IDFM:44371 - Place D'italie
        // IDFM:42587 - Chatelet

        StationFinder stationFinder = new StationFinder();
        stationFinder.prim();
        stationFinder.printfindBestPath(new ArrayList<String>(Arrays.asList( "Ch\u00e2telet", "Villejuif Louis Aragon","Bastille", "Gare de l'Est")));

        
    }
}