import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.*;
 
public class Graph {
    //Find station by ID IDFM easly TA M7RE DIEGO
    HashMap<String, Station> stations = new HashMap<>();

    ArrayList<Station> stationsList = new ArrayList<>();
    HashMap<String, Ligne> lignes = new HashMap<>();
    ArrayList<Edge> edges = new ArrayList<>();
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
                        formatted_station = convertLineType(station, Integer.parseInt(this.datas.station_csv.get(i).get(0)));
                        station_id = formatted_station.idfm_station_id;

                        for (JSON_Ligne ligne : this.datas.data_ligne_const) {
                            if (this.datas.station_csv.get(i).get(1).equals(ligne.name_line.toLowerCase())) {
                                formatted_station.setLigne(
                                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                                );
                                break;
                            }
                        }

                        // Ajout des neighbours
                        formatted_station = addingNeighbors(formatted_station, station, i);

                        // Ajout du formatted_station dans la ArrayList station
                        if (!this.stations.containsKey(station_id)) {
                            this.stations.put(station_id, formatted_station);
                        } else {
                            for (int edge_index = 0; edge_index < this.stations.get(station_id).getNeighbors().size(); edge_index++) {
                                Edge edge = this.stations.get(station_id).getNeighbors().get(edge_index);
                                formatted_station.setNeighbors(edge);
                            }
                            this.stations.replace(station_id, formatted_station);
                        }

                        this.stationsList.add(formatted_station);
                    }
                }
            }
        }

        removeIsolatedStations();
    }

    private void removeIsolatedStations() {
        for (int index = 0; index < stationsList.size(); index++) {
            int nbNeighbors = stationsList.get(index).getNeighbors().size();
            if (nbNeighbors == 0) {
                stationsList.remove(index);
                index--;
            }
        }
    }

    private Station addingNeighbors(Station formatted_station, JSON_Station station, int i) {
        for (int j = 1; j < this.datas.relation_csv.size(); j++) {
            // Regarde si l'ID de la ligne correspond à celui dans relation
            if (this.datas.relation_csv.get(j).get(0).equals(this.datas.station_csv.get(i).get(0))) {
                String id_other = getCorrectRelationID(i, j);

                // Regarde si l'ID de la ligne a une relation avec une autre ligne
                for (JSON_Station station_2 : this.datas.data_stations_const) {
                    if (station.metro == 1) {
                        if (Double.parseDouble((String) this.datas.hashMap_station_csv.get(id_other).get(4)) == station_2.id_ref_zdl) {
                            Station other_formatted_station = insertLinesToOtherStation(convertLineType(station_2, Integer.parseInt((String) this.datas.hashMap_station_csv.get(id_other).get(0))), (String) this.datas.hashMap_station_csv.get(id_other).get(0));

                            Edge new_edge = insertLinesToEdge(new Edge(
                                    formatted_station,
                                    other_formatted_station,
                                    Integer.parseInt(this.datas.relation_csv.get(j).get(2))
                            ), (String) this.datas.hashMap_station_csv.get(id_other).get(0));

                            formatted_station.setNeighbors(new_edge);
                            this.edges.add(new_edge);
                        }
                    }
                }
            }
        }

        return formatted_station;
    }

    public ArrayList<Edge> getNeighbors(int currentNode) {
        ArrayList<Edge> neighbors = new ArrayList<>();
        for (Edge edge : this.edges) {
            if (edge.containStation(this.stationsList.get(currentNode))) {
                neighbors.add(edge);
            }
        }
        return neighbors;
    }

    public ArrayList<Edge> getNeighbors(Station station) {
        ArrayList<Edge> neighbors = new ArrayList<>();
        for (Edge edge : this.edges) {
            if (edge.containStation(station)) {
                neighbors.add(edge);
            }
        }
        return neighbors;
    }

    public Station getStationByID(int station_id) {
        for (Station station : this.stationsList) {
            if (station.getID() == station_id) {
                return station;
            }
        }
        return null;
    }
    

    public HashMap<String, Station> getStations() {
        return this.stations;
    }

    private static Station convertLineType(JSON_Station station, int station_id) {
        Station formatted_station = new Station(station_id,"IDFM:" + (int) station.id_ref_zdl, station.nom_zdl, station.geo_shape.geometry.coordinates);

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
            if (this.datas.hashMap_station_csv.get(id_other).get(1).equals(ligne.name_line.toLowerCase())) {
                new_edge.setLigne(
                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                );
                break;
            }
        }

        return new_edge;
    }

    private Station insertLinesToOtherStation(Station other_formatted_station, String id_other) {
        for (JSON_Ligne ligne : this.datas.data_ligne_const) {
            if (this.datas.hashMap_station_csv.get(id_other).get(1).equals(ligne.name_line.toLowerCase())) {
                other_formatted_station.setLigne(
                        new Ligne(ligne.id_line, ligne.transportmode, ligne.name_line)
                );
                break;
            }
        }

        return other_formatted_station;
    }

    public ArrayList<Station> getStationsList() {
        return this.stationsList;
    }

    public String stationligne(Ligne l) {
        return l.toString();
    }

    public HashSet<Station> correspondancelignes(Ligne l1, Ligne l2) {
        HashSet<Station> cor = new HashSet<Station>();
        // Crée algo, donnant les correspondances entre 2 lignes
        return cor;        
    }

    public ArrayList<Station> findStationsByName(String stationName) {
        ArrayList<Station> stations = new ArrayList<>();
        for (Station station : this.stationsList) {
            if (station.getName().equals(stationName)) {
                stations.add(station);
            }
        }
        return stations;
    }
    /* 
    public void primcleaner() {
        Prim prim = new Prim(this);
        for(int i =0; i < this.edges.size(); i++) {
            Edge e = this.edges.get(i);
            if(!prim.getEdges().contains(e)) {
                //Remove edge from station
                e.getArrivalStation().getNeighbors().remove(e);
                e.getDepartureStation().getNeighbors().remove(e);

                //Remove edge from arraylist of edges
                this.edges.remove(e);

                //Remove Stations
                this.stations.remove(e.getArrivalStation().getName(), e);
                this.stations.remove(e.getDepartureStation().getName(), e);
                try {
                    e.getArrivalStation().getLigne().stations.remove(e.getArrivalStation());
                    e.getDepartureStation().getLigne().stations.remove(e.getDepartureStation());
                } catch(NullPointerException error) {}
                
                this.stationsList.remove(e.getArrivalStation());
                this.stationsList.remove(e.getArrivalStation());
                i--;
            }
        }
    }
    */
    public void primcleaner() {
        Prim prim = new Prim(this);

        this.edges = prim.getEdges();
        HashSet<Station> stations = new HashSet<Station>();
        for(Edge i : prim.getEdges()){
            stations.add(i.getArrivalStation()); 
            stations.add(i.getDepartureStation());
        }
        this.stationsList = new ArrayList<Station>(stations);
        
        /*for (Station st : this.stationsList) {
            for (Station st2 : this.stationsList) {
                if (st.getName().equals(st2.getName())) {
                    System.out.println(st);
                }
            }
        }*/
    }
}