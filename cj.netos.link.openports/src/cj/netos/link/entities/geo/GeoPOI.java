package cj.netos.link.entities.geo;

import org.bson.Document;

import java.util.Map;

public class GeoPOI {
    GeoReceptor receptor;
    double distance;

    public static GeoPOI parse(Document doc) {
        GeoPOI poi=new GeoPOI();
        Map<String,Object> tuple=(Map<String,Object>)doc.get("tuple");
        poi.setDistance((double)tuple.get("distance"));
        poi.receptor=GeoReceptor.parse(tuple);
        return poi;
    }

    public GeoReceptor getReceptor() {
        return receptor;
    }

    public void setReceptor(GeoReceptor receptor) {
        this.receptor = receptor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
