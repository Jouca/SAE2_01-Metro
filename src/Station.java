import java.util.ArrayList;

public class Station {
    String station_id;
    String name;
    ArrayList<Double> coordinates;
    ArrayList<Ligne> lignes;

    //Distance and neighbor it's use for Disjkra
    Integer distance = Integer.MAX_VALUE;
    ArrayList<Edge> neighbor = new ArrayList<Edge>();

    Station(String station_id, String name, ArrayList<Double> coordinates) {
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

    public ArrayList<Double> getCoordinates() {
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
    
}
