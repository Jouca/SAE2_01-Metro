import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
        // Test Station JSON

        final Type JSON_STATION_TYPE = new TypeToken<List<JSON_Station>>() {
        }.getType();
        Gson gson_station = new Gson();
        JsonReader reader_station = new JsonReader(new FileReader("data/emplacement-des-gares-idf-data-generalisee.json"));
        final List<JSON_Station> data_stations_const = gson_station.fromJson(reader_station, JSON_STATION_TYPE);

        // Test Ligne JSON

        final Type JSON_LIGNE_TYPE = new TypeToken<List<JSON_Ligne>>() {
        }.getType();
        Gson gson_ligne = new Gson();
        JsonReader reader_ligne = new JsonReader(new FileReader("data/referenciel-des-lignes.json"));
        final List<JSON_Ligne> data_ligne_const = gson_ligne.fromJson(reader_ligne, JSON_LIGNE_TYPE);

        // Test Trace Ligne JSON

        final Type JSON_TRACE_LIGNE_TYPE = new TypeToken<JSON_Trace_Ligne>() {
        }.getType();
        Gson gson_trace_ligne = new Gson();
        JsonReader reader_trace_ligne = new JsonReader(new FileReader("data/traces-du-reseau-ferre-idf.geojson"));
        final JSON_Trace_Ligne data_trace_ligne_const = gson_trace_ligne.fromJson(reader_trace_ligne, JSON_TRACE_LIGNE_TYPE);
        /*for (Feature feature : data_trace_ligne_const.features) {
            for (List<Double> geometry : feature.geometry.coordinates) {
                System.out.println(geometry);
            }
        }*/

        // Test Stations CSV

        List<List<String>> station_csv = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("data/stations.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                ).build()) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                station_csv.add(Arrays.asList(values));
            }
        }

        // Test Relations CSV

        List<List<String>> relation_csv = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("data/relations.csv"))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                ).build()) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                relation_csv.add(Arrays.asList(values));
            }
        }


        HashMap<String, Station> stations = new HashMap<>();
        HashMap<String, Ligne> lignes = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Graph graphe = new Graph();

        int count = 1;

        for (int i = 1; i < station_csv.size(); i++) {
            for (JSON_Station station : data_stations_const) {
                Station formatted_station = null;
                String station_id = "";

                if (Double.parseDouble(station_csv.get(i).get(4)) == station.id_ref_lda || Double.parseDouble(station_csv.get(i).get(4)) == station.id_ref_zdl) {
                    if (!stations.containsKey("IDFM:monomodalStopPlace:" + (int) station.id_ref_zdl) && !stations.containsKey("IDFM:" + (int) station.id_ref_lda)) {
                        if (station.train == 1 || station.rer == 1) {
                            formatted_station = new Station("IDFM:monomodalStopPlace:" + (int) station.id_ref_zdl, station.nom_long, station.geo_shape.geometry.coordinates);
                            station_id = "IDFM:monomodalStopPlace:" + (int) station.id_ref_zdl;
                        } else {
                            formatted_station = new Station("IDFM:" + (int) station.id_ref_lda, station.nom_long, station.geo_shape.geometry.coordinates);
                            station_id = "IDFM:" + (int) station.id_ref_lda;
                        }
                    }

                    // Ajout des neighbours
                    for (int j = 1; j < relation_csv.size(); j++) {
                        if (relation_csv.get(j).get(0).equals(station_csv.get(i).get(0))) {
                            for (int q = 1; q < station_csv.size(); q++) {
                                if (relation_csv.get(j).get(1).equals(station_csv.get(q).get(0))) {
                                    System.out.println(relation_csv.get(j) + " " + count);
                                    count += 1;
                                }
                            }
                        }
                    }
                }

                stations.put(station_id, formatted_station);
            }
        }

        //System.out.println(stations.get("IDFM:71359").getLignes());
    }
}