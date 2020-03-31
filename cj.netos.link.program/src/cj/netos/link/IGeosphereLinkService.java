package cj.netos.link;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.geo.GeoPOD;
import cj.netos.link.entities.geo.GeoPOF;
import cj.netos.link.entities.geo.GeoPOI;
import cj.netos.link.entities.geo.GeoReceptor;

import java.util.List;

public interface IGeosphereLinkService {
    List<GeoPOI> searchAroundReceptors(String person,GeoReceptor geoReceptor, String geoType, long limit, long skip);

    GeoReceptor getReceptor(String category, String receptor);

    List<GeoPOD> searchAroundDocuments(String person,GeoReceptor geoReceptor, String geoType, long limit, long skip);


    void followReceptor(GeoReceptor geoReceptor, String follower);

    void unfollowReceptor(GeoReceptor geoReceptor, String follower);

    List<GeoPOF> pageReceptorFans(String person,GeoReceptor geoReceptor, long limit, long skip);

    List<Channel> listReceptorChannels(String principal);

}
