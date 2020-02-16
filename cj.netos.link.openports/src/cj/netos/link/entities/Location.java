package cj.netos.link.entities;

import java.util.List;

public class Location {
    GeoType type;
    List<Coordinate> coordinates;

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
