package cj.netos.link;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.geo.*;

import java.util.List;

public interface IGeosphereLinkService {
    List<GeoPOI> searchAroundReceptors(String person,GeoReceptor geoReceptor, String geoType, long limit, long skip);

    GeoReceptor getReceptor( String receptor);

    List<GeoPOD> searchAroundDocuments(String person,GeoReceptor geoReceptor, String geoType, long limit, long skip);


    void followReceptor(GeoReceptor geoReceptor, String follower);

    void unfollowReceptor(GeoReceptor geoReceptor, String follower);

    List<GeoPOF> pageReceptorFans(String person,GeoReceptor geoReceptor, long limit, long skip);

    List<Channel> listReceptorChannels(String principal);

    long countReceptorFans( GeoReceptor geoReceptor);

    List<GeoPOI> searchAroundLocation(String principal, LatLng location, double radius, String geoType, long limit, long skip);

    List<GeoPOI> searchPersonAroundLocation(String principal, LatLng location, double radius, String geoType, String person);

    void updateFollowRights(GeoReceptor geoReceptor, String fans,String rights);

    boolean isDenyFollowSpeak(GeoReceptor geoReceptor, String principal);

}
