import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Test Station JSON

        final Type JSON_STATION_TYPE = new TypeToken<List<JSON_Station>>() {
        }.getType();
        Gson gson_station = new Gson();
        JsonReader reader_station = new JsonReader(new FileReader("data/emplacement-des-gares-idf-data-generalisee.json"));
        final List<JSON_Station> data_stations_const = gson_station.fromJson(reader_station, JSON_STATION_TYPE);
        /*for (JSON_Station station : data_stations_const) {
            System.out.println(station.nom_long);
        }*/

        // Test Ligne JSON

        final Type JSON_LIGNE_TYPE = new TypeToken<List<JSON_Ligne>>() {
        }.getType();
        Gson gson_ligne = new Gson();
        JsonReader reader_ligne = new JsonReader(new FileReader("data/referenciel-des-lignes.json"));
        final List<JSON_Ligne> data_ligne_const = gson_ligne.fromJson(reader_ligne, JSON_LIGNE_TYPE);
        /*for (JSON_Ligne ligne : data_ligne_const) {
            System.out.println(ligne.name_line);
        }*/

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


        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Ligne> lignes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Graph graphe = new Graph();

        for (JSON_Station station : data_stations_const) {
            Station formatted_station;
            if (station.train == 1 || station.rer == 1) {
                formatted_station = new Station("IDFM:monomodalStopPlace:"+(int)station.id_ref_zdl, station.nom_long, station.geo_shape.geometry.coordinates);
            } else {
                formatted_station = new Station("IDFM:"+(int)station.id_ref_lda, station.nom_long, station.geo_shape.geometry.coordinates);
            }
        }

        Station test = stations.get(19);
        System.out.println(test.name);
        System.out.println(test.lignes);

        for (Edge edge_test : test.neighbor) {
            System.out.println(edge_test.parents.get(0).station_id + " " + edge_test.parents.get(1).station_id);
        }
    }
}