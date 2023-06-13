import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DatasBuilder {
    public List<JSON_Station> data_stations_const = new ArrayList<>();
    public List<JSON_Ligne> data_ligne_const = new ArrayList<>();
    public JSON_Trace_Ligne data_trace_ligne_const = null;
    public List<List<String>> station_csv = new ArrayList<>();
    public HashMap<String, List<String> > hashMap_station_csv = new HashMap<>();
    public List<List<String>> relation_csv = new ArrayList<>();


    DatasBuilder() throws IOException, CsvValidationException {
        // Test Station JSON
        final Type JSON_STATION_TYPE = new TypeToken<List<JSON_Station>>() {
        }.getType();
        Gson gson_station = new Gson();
        JsonReader reader_station = new JsonReader(new FileReader("data/emplacement-des-gares-idf-data-generalisee.json"));
        this.data_stations_const = gson_station.fromJson(reader_station, JSON_STATION_TYPE);

        // Test Ligne JSON
        final Type JSON_LIGNE_TYPE = new TypeToken<List<JSON_Ligne>>() {
        }.getType();
        Gson gson_ligne = new Gson();
        JsonReader reader_ligne = new JsonReader(new FileReader("data/referenciel-des-lignes.json"));
        this.data_ligne_const = gson_ligne.fromJson(reader_ligne, JSON_LIGNE_TYPE);

        // Test Trace Ligne JSON
        final Type JSON_TRACE_LIGNE_TYPE = new TypeToken<JSON_Trace_Ligne>() {
        }.getType();
        Gson gson_trace_ligne = new Gson();
        JsonReader reader_trace_ligne = new JsonReader(new FileReader("data/traces-du-reseau-ferre-idf.geojson"));
        this.data_trace_ligne_const = gson_trace_ligne.fromJson(reader_trace_ligne, JSON_TRACE_LIGNE_TYPE);

        // Test Stations CSV
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("data/stations.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                ).build()) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                this.station_csv.add(Arrays.asList(values));
            }
        }

        for (List<String> list : this.station_csv) {
            this.hashMap_station_csv.put((String) list.get(0), list);
        }

        // Test Relations CSV
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("data/relations.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                ).build()) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                this.relation_csv.add(Arrays.asList(values));
            }
        }
    }
}
