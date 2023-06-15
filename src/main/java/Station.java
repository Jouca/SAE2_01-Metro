import java.util.ArrayList;
import java.util.List;

public class Station {
    String idfm_station_id;
    int station_id;
    String name;
    List<Double> coordinates;
    Ligne ligne;

    //Distance and neighbor it's use for Disjkra
    Integer distance = Integer.MAX_VALUE;
    ArrayList<Edge> neighbors = new ArrayList<>();

    public Station(int station_id, String idfm_station_id, String name, List<Double> coordinates) {
        this.station_id = station_id;
        this.idfm_station_id = idfm_station_id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public ArrayList<Edge> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Edge edge) {
        this.neighbors.add(edge);
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getIDFMID() {
        return idfm_station_id;
    }
    public int getID() {return station_id;}

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

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    @Override
    public String toString() {
        return "intID=" + this.station_id+ " ,Station_id=" + this.idfm_station_id + ", Name=" + this.name + ", Coordinates" + this.coordinates + ", Lines=" + this.ligne;
    }

    @Override
    public boolean equals(Object otherStation) {
        if (!(otherStation instanceof Station)) return false;
        return this.getID() == ((Station) otherStation).getID();
    }
}
