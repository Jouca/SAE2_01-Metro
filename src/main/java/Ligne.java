package main.java;

import java.util.ArrayList;

public class Ligne {
    String line_id;
    String type;
    String name;
    ArrayList<Station> stations;
    ArrayList<Station> terminus;

    Ligne(String line_id, String type, String name) {
        this.line_id = line_id;
        this.type = type;
        this.name = name;
    }

    public Boolean contains(Station s) {
        return this.stations.contains(s);
    }

    public Boolean isTerminus(Station s) {
        return this.terminus.contains(s);
    }

    public String getID() {
        return this.line_id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public ArrayList<Station> getStations() {
        return this.stations;
    }

    public ArrayList<Station> getTerminus() {
        return this.terminus;
    }

    public void addStation(Station s) {
        this.stations.add(s);
    }

    public void addTerminus(Station s) {
        if (!this.isTerminus(s)) {
            this.terminus.add(s);
        }
    }

    @Override
    public String toString() {
        return "Ligne [line_id=" + line_id + ", type=" + type + ", name=" + name + ", stations=" + stations
                + ", terminus=" + terminus + "]";
    }
}
