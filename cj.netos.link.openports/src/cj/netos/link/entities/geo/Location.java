package cj.netos.link.entities.geo;

import java.util.ArrayList;
import java.util.List;

public class Location {
    GeoType type;
    List<Coordinate> coordinates;

    public Location() {
        coordinates = new ArrayList<>();
    }

    public Location(GeoType type, List<Coordinate> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
        if (this.coordinates == null) {
            this.coordinates = new ArrayList<>();
        }
    }

    public GeoType getType() {
        return type;
    }

    public void setType(GeoType type) {
        this.type = type;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
