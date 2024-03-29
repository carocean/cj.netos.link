package cj.netos.link.ports;

import cj.netos.link.IGeosphereLinkService;
import cj.netos.link.entities.Channel;
import cj.netos.link.entities.geo.*;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/geosphere/self.service")
public class GeosphereLinkPorts implements IGeosphereLinkPorts {
    @CjServiceRef
    IGeosphereLinkService geosphereLinkService;

    @Override
    public List<GeoPOI> searchAroundReceptors(ISecuritySession securitySession,  String receptor, String geoType,long limit,long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
        return geosphereLinkService.searchAroundReceptors(securitySession.principal(),geoReceptor, geoType,limit,skip);
    }

    @Override
    public List<GeoPOD> searchAroundDocuments(ISecuritySession securitySession, String receptor,String geoType,long limit,long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
        return geosphereLinkService.searchAroundDocuments(securitySession.principal(),geoReceptor,geoType,limit,skip);
    }

    @Override
    public List<GeoPOI> searchAroundLocation(ISecuritySession securitySession, LatLng location, double radius, String geoType, long limit, long skip) throws CircuitException {
        if (location == null) {
            throw new CircuitException("404", String.format("位置为空"));
        }
        if (radius <= 0) {
            throw new CircuitException("500","半径小于等于0");
        }

        return geosphereLinkService.searchAroundLocation(securitySession.principal(),location,radius,geoType,limit,skip);
    }

    @Override
    public List<GeoPOI> searchPersonAroundLocation(ISecuritySession securitySession, LatLng location, double radius, String geoType, String person) throws CircuitException {
        if (location == null) {
            throw new CircuitException("404", String.format("位置为空"));
        }
        if (radius <= 0) {
            throw new CircuitException("500","半径小于等于0");
        }

        return geosphereLinkService.searchPersonAroundLocation(securitySession.principal(),location,radius,geoType,person);
    }

    @Override
    public void followReceptor(ISecuritySession securitySession,  String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor( receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
         geosphereLinkService.followReceptor(geoReceptor,securitySession.principal());
    }

    @Override
    public void unfollowReceptor(ISecuritySession securitySession,  String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
         geosphereLinkService.unfollowReceptor(geoReceptor,securitySession.principal());
    }

    @Override
    public void allowFollowSpeak(ISecuritySession securitySession, String receptor,String fans) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
        if (!securitySession.principal().equals(geoReceptor.getCreator())) {
            throw new CircuitException("801", String.format("不是感知器拥有者，禁止访问。"));
        }
        geosphereLinkService.updateFollowRights(geoReceptor,fans,"allowSpeak");
    }

    @Override
    public void denyFollowSpeak(ISecuritySession securitySession, String receptor,String fans) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
        if (!securitySession.principal().equals(geoReceptor.getCreator())) {
            throw new CircuitException("801", String.format("不是感知器拥有者，禁止访问。"));
        }
        geosphereLinkService.updateFollowRights(geoReceptor,fans,"denySpeak");
    }

    @Override
    public boolean isDenyFollowSpeak(ISecuritySession securitySession, String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(receptor);
        if (geoReceptor == null) {
            return false;
        }
        return geosphereLinkService.isDenyFollowSpeak(geoReceptor,securitySession.principal());
    }

    @Override
    public List<GeoPOF> pageReceptorFans(ISecuritySession securitySession, String receptor, long limit, long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor( receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("不存在地理感知器：%s。", receptor));
        }
        return geosphereLinkService.pageReceptorFans(securitySession.principal(),geoReceptor,limit,skip);
    }

    @Override
    public long countReceptorFans(ISecuritySession securitySession,  String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor( receptor);
        if (geoReceptor == null) {
            return 0;
        }
        return geosphereLinkService.countReceptorFans(geoReceptor);
    }

    @Override
    public List<Channel> listReceptorChannels(ISecuritySession securitySession) throws CircuitException {
        return geosphereLinkService.listReceptorChannels(securitySession.principal());
    }
}
