public class Edge {

    Station departureStation;
    Station arrivalStation;
    Ligne ligne;
    int timeWeight;

    Edge(Station from_station, Station to_station, int time) {
        this.departureStation = from_station;
        this.arrivalStation = to_station;
        this.timeWeight = time;
    }


    public int getTimeWeight() {
        return timeWeight;
    }

    public Station getOtherParent(Station station) {
        return (station.equals(departureStation)) ? arrivalStation : departureStation;
    }

    public Station getArrivalStation() {
        return this.arrivalStation;
    }

    public Station getDepartureStation() {
        return this.departureStation;
    }

    public Boolean containStation(Station station) {
        return station.equals(arrivalStation) || station.equals(departureStation);
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    @Override
    public String toString() {
        return "Departure=" + this.departureStation + ", Arrival=" + this.arrivalStation + ", Lines=" + this.ligne + ", Weight=" + this.timeWeight;
    }
}