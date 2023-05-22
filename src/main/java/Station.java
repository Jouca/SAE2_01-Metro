package main.java;

import java.util.ArrayList;
import java.util.List;

public class Station {
    String station_id;
    String name;
    List<Double> coordinates;
    ArrayList<Ligne> lignes = new ArrayList<>();

    //Distance and neighbor it's use for Disjkra
    Integer distance = Integer.MAX_VALUE;
    ArrayList<Edge> neighbor = new ArrayList<>();

    Station(String station_id, String name, List<Double> coordinates) {
        this.station_id = station_id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public Boolean contains(Ligne l){
        return lignes.contains(l);
    }

    public ArrayList<Edge> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(Edge edge) {
        this.neighbor.add(edge);
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getID() {
        return station_id;
    }

    public String getName() {
        return this.name;
    }

    public List<Double> getCoordinates() {
        return this.coordinates;
    }

    public Double getLatitude() {
        return this.coordinates.get(0);
    }

    public Double getLongitude() {
        return this.coordinates.get(1);
    }

    public ArrayList<Ligne> getLignes() {
        return lignes;
    }

    public void setLignes(Ligne ligne) {
        this.lignes.add(ligne);
    }
}
