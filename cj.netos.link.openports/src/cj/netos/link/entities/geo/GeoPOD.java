package cj.netos.link.entities.geo;

import org.bson.Document;

import java.util.Map;

/**
 * 表示地理文档
 */
public class GeoPOD {
    GeoDocument document;
    double distance;

    public static GeoPOD parse(Document doc, GeoCategory category) {
        GeoPOD pod=new GeoPOD();
        Map<String,Object> tuple=(Map<String,Object>)doc.get("tuple");
        pod.setDistance((double)tuple.get("distance"));
        pod.document=GeoDocument.parse(tuple);
        pod.document.category=category.getId();
        return pod;
    }

    public GeoDocument getDocument() {
        return document;
    }

    public void setDocument(GeoDocument document) {
        this.document = document;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
