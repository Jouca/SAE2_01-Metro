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
    private static Station convertLineType(JSON_Station station) {
        Station formatted_station = null;

        if (station.train == 1 || station.rer == 1) {
            formatted_station = new Station("IDFM:monomodalStopPlace:" + (int) station.id_ref_lda, station.nom_long, station.geo_shape.geometry.coordinates);
        } else {
            formatted_station = new Station("IDFM:" + (int) station.id_ref_zdl, station.nom_long, station.geo_shape.geometry.coordinates);
        }

        return formatted_station;
    }
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

        HashMap<String, List> hashMap_station_csv = new HashMap<>();
        for (List list : station_csv) {
            hashMap_station_csv.put((String) list.get(0), list);
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

        List<String> transport_allow = new ArrayList<>();
        transport_allow.add("METRO");


        HashMap<String, Station> stations = new HashMap<>();
        HashMap<String, Ligne> lignes = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Graph graphe = new Graph();

        for (int i = 1; i < station_csv.size(); i++) {
            for (JSON_Station station : data_stations_const) {
                if (station.metro == 1) {
                    Station formatted_station = null;
                    String station_id = "";

                    if (Double.parseDouble(station_csv.get(i).get(4)) == station.id_ref_zdl) {
                        formatted_station = convertLineType(station);
                        station_id = formatted_station.station_id;

                        for (JSON_Ligne ligne : data_ligne_const) {
                            if (station.idrefligc.contains(ligne.id_line)) {
                                formatted_station.setLignes(
                                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                                );
                            }
                        }

                        // Ajout des neighbours
                        for (int j = 1; j < relation_csv.size(); j++) {
                            // Regarde si l'ID de la ligne correspond à celui dans relation
                            if (relation_csv.get(j).get(0).equals(station_csv.get(i).get(0))) {
                                String id_other = relation_csv.get(j).get(1);

                                // Regarde si l'ID de la ligne a une relation avec une autre ligne
                                for (JSON_Station station_2 : data_stations_const) {
                                    if (station.metro == 1) {
                                        if (Double.parseDouble((String) hashMap_station_csv.get(id_other).get(4)) == station_2.id_ref_zdl) {
                                            Station other_formatted_station = convertLineType(station_2);

                                            for (JSON_Ligne ligne : data_ligne_const) {
                                                if (station_2.idrefligc.contains(ligne.id_line)) {
                                                    other_formatted_station.setLignes(
                                                            new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                                                    );
                                                }
                                            }

                                            Edge new_edge = new Edge(
                                                    formatted_station,
                                                    other_formatted_station,
                                                    Integer.parseInt(relation_csv.get(j).get(2))
                                            );

                                            for (JSON_Ligne ligne : data_ligne_const) {
                                                if (ligne.name_line.equals(hashMap_station_csv.get(id_other).get(1))) {
                                                    new_edge.setLigne(
                                                            new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                                                    );
                                                }
                                            }

                                            formatted_station.setNeighbor(new_edge);
                                        }
                                    }
                                }
                            }
                        }

                        if (!stations.containsKey("IDFM:" + (int) station.id_ref_zdl)) {
                            stations.put(station_id, formatted_station);
                        } else {
                            for (Edge edge : stations.get(station_id).getNeighbor()) {
                                formatted_station.setNeighbor(edge);
                            }
                            stations.replace(station_id, formatted_station);
                        }
                    }
                }
            }
        }

        for (Station station : stations.values()) {
            graphe.addstation(station);
        }


        System.out.println(graphe.Dijkstra(stations.get("IDFM:42587"), stations.get("IDFM:44371")));

        /*for (Edge edge : stations.get("IDFM:44371").getNeighbor()) {
            System.out.println(edge.parents);
        }*/
    }
}