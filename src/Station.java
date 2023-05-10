import java.util.ArrayList;

public class Station {
    String station_id;
    String name;
    ArrayList<Double> coordinates;

    Station(String station_id, String name, ArrayList<Double> coordinates) {
        this.station_id = station_id;
        this.name = name;
        this.coordinates = coordinates;
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
}
