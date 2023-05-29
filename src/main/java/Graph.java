import com.google.gson.internal.bind.DateTypeAdapter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.*;

public class Graph {
    HashMap<String, Station> stations = new HashMap<>();
    HashMap<String, Ligne> lignes = new HashMap<>();
    ArrayList<Edge> edges = new ArrayList<>();

    HashMap<Station, Edge> anterior = new HashMap<>();
    DatasBuilder datas = new DatasBuilder();

    Graph() throws CsvValidationException, IOException {
        List<String> transport_allow = new ArrayList<>();
        transport_allow.add("METRO");


        for (int i = 1; i < this.datas.station_csv.size(); i++) {
            if (this.datas.station_csv.get(i).contains("Id;station")) continue;

            for (JSON_Station station : this.datas.data_stations_const) {
                if (station.metro == 1) {
                    Station formatted_station = null;
                    String station_id = "";

                    if (Double.parseDouble(this.datas.station_csv.get(i).get(4)) == station.id_ref_zdl) {
                        formatted_station = convertLineType(station);
                        station_id = formatted_station.station_id;

                        for (JSON_Ligne ligne : this.datas.data_ligne_const) {
                            if (station.idrefligc.contains(ligne.id_line)) {
                                formatted_station.setLignes(
                                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                                );
                            }
                        }

                        // Ajout des neighbours
                        addingNeighbors(formatted_station, station, i);

                        // Ajout du formatted_station dans la ArrayList station
                        if (!this.stations.containsKey(station_id)) {
                            this.stations.put(station_id, formatted_station);
                        } else {
                            for (int edge_index = 0; edge_index < this.stations.get(station_id).getNeighbor().size(); edge_index++) {
                                Edge edge = this.stations.get(station_id).getNeighbor().get(edge_index);
                                formatted_station.setNeighbor(edge);
                            }
                            this.stations.replace(station_id, formatted_station);
                        }
                    }
                }
            }
        }
    }

    private Station addingNeighbors(Station formatted_station, JSON_Station station, int i) {
        for (int j = 1; j < this.datas.relation_csv.size(); j++) {
            // Regarde si l'ID de la ligne correspond à celui dans relation
            if (this.datas.relation_csv.get(j).get(0).equals(this.datas.station_csv.get(i).get(0)) || this.datas.relation_csv.get(j).get(1).equals(this.datas.station_csv.get(i).get(0))) {
                String id_other = getCorrectRelationID(i, j);

                // Regarde si l'ID de la ligne a une relation avec une autre ligne
                for (JSON_Station station_2 : this.datas.data_stations_const) {
                    if (station.metro == 1) {
                        if (Double.parseDouble((String) this.datas.hashMap_station_csv.get(id_other).get(4)) == station_2.id_ref_zdl) {
                            Station other_formatted_station = insertLinesToOtherStation(convertLineType(station_2), station_2);

                            Edge new_edge = insertLinesToEdge(new Edge(
                                    formatted_station,
                                    other_formatted_station,
                                    Integer.parseInt(this.datas.relation_csv.get(j).get(2))
                            ), id_other);

                            formatted_station.setNeighbor(new_edge);
                        }
                    }
                }
            }
        }

        return formatted_station;
    }

    private static Station convertLineType(JSON_Station station) {
        Station formatted_station = null;

        if (station.train == 1 || station.rer == 1) {
            formatted_station = new Station("IDFM:monomodalStopPlace:" + (int) station.id_ref_lda, station.nom_long, station.geo_shape.geometry.coordinates);
        } else {
            formatted_station = new Station("IDFM:" + (int) station.id_ref_zdl, station.nom_long, station.geo_shape.geometry.coordinates);
        }

        return formatted_station;
    }

    private String getCorrectRelationID(int i, int j) {
        if (this.datas.relation_csv.get(j).get(0).equals(this.datas.station_csv.get(i).get(0))) {
            return this.datas.relation_csv.get(j).get(1);
        } else {
            return this.datas.relation_csv.get(j).get(0);
        }
    }

    private Edge insertLinesToEdge(Edge new_edge, String id_other) {
        for (JSON_Ligne ligne : this.datas.data_ligne_const) {
            if (ligne.name_line.equals(this.datas.hashMap_station_csv.get(id_other).get(1))) {
                new_edge.setLigne(
                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                );
            }
        }

        return new_edge;
    }

    private Station insertLinesToOtherStation(Station other_formatted_station, JSON_Station station_2) {
        for (JSON_Ligne ligne : this.datas.data_ligne_const) {
            if (station_2.idrefligc.contains(ligne.id_line)) {
                other_formatted_station.setLignes(
                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                );
            }
        }

        return other_formatted_station;
    }

    public ArrayList<Edge> Dijkstra(Station begin, Station end){
        begin.setDistance(0);
        ArrayList<Station> nodes = new ArrayList<>((Collection) stations);

        ////////////////////////////////////////
        // BOUCLE INFINIE
        while (!nodes.isEmpty()) {
            Station s1 = minneighbor(nodes);
            nodes.remove(s1);
            for(Edge e : s1.getNeighbor()) {
                update_weight(e.getDepartureStation(), e.getArrivalStation());
            }
        }
        ///////////////////////////////////////

        System.out.println("etape 2");

        for (Edge i : anterior.values()) {
            System.out.println(i);
        }

        ArrayList<Edge> a = new ArrayList<>();
        while(end != begin) {
            a.add(0, anterior.get(end));
            end = anterior.get(end).getOtherParent(end);
        }

        return a;
    }

    private Station minneighbor(ArrayList<Station> set) {
        Integer mini = Integer.MAX_VALUE;
        Station sommet = null;

        for (Station s : set) {
            for (Edge i : s.getNeighbor()) {
                if (i.getTimeWeight() < mini) {
                    mini = i.getTimeWeight();
                    sommet = i.getOtherParent(s);
                }
            }
        }
        return sommet;
    }

    private void update_weight(Station s1, Station s2) {
        Edge miniedge = getminiedge(s1, s2);
        if(s2.getDistance() > (s1.getDistance() + miniedge.getTimeWeight())){
            s2.setDistance((s1.getDistance() + miniedge.getTimeWeight()));
            anterior.put(s2, miniedge);
        }
    }

    public Edge getminiedge(Station s1, Station s2) {
        Integer mini = Integer.MAX_VALUE;
        Edge temp = null;
        for(Edge i : s1.getNeighbor()) {
            /*
            if (i.containStation(s1) && i.containStation(s2) && i.getLigne().equals(anterior.get(s1).getLigne())){
                temp = i;
                break;
            }
            else if(i.containStation(s1) && i.containStation(s2) && i.getWeight() < mini) {
                mini = i.getWeight();
                temp = i;
            }*/
            if (i.containStation(s1) && i.containStation(s2)) {
                mini = i.getTimeWeight();
                temp = i;
            }
        }
        return temp;
    }

    public String stationligne(Ligne l) {
        return l.toString();
    }

    public String correspondance() {
        HashSet<Ligne> cor = new HashSet<Ligne>();

        for(Station s : stations.values()) {
            for(Ligne i : s.getLignes()){
                cor.add(i);
            }
        }

        return cor.toString();        
    }

    public String correspondanceligne(Ligne l) {
        HashSet<Ligne> cor = new HashSet<Ligne>();

        for(Station s : l.getStations()) {
            if(stations.values().contains(s)){
                for(Ligne i : s.getLignes()){
                    cor.add(i);
                }
            }
        }

        return cor.toString();        
    }

}