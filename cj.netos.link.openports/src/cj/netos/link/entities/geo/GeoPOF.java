package cj.netos.link.entities.geo;

import cj.netos.link.entities.PersonInfo;

/**
 * 感知器粉丝
 */
public class GeoPOF {
    GeoFollow follow;
    double distance;

    public GeoFollow getFollow() {
        return follow;
    }

    public void setFollow(GeoFollow follow) {
        this.follow = follow;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
