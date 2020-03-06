package cj.netos.link.entities.geo;

import java.util.Arrays;
import java.util.List;

public class Coordinate {
    double longitude;//经度
    double latitude;//纬度

    public Coordinate() {
    }

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public List<Double> toCoordinate() {
        return Arrays.asList(this.longitude, this.latitude);
    }
}
