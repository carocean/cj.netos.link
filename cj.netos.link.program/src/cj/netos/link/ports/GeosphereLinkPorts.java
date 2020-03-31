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
    public List<GeoPOI> searchAroundReceptors(ISecuritySession securitySession, String category, String receptor, String geoType,long limit,long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(category, receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("分类<%s>不存在地理感知器：%s。", category, receptor));
        }
        return geosphereLinkService.searchAroundReceptors(securitySession.principal(),geoReceptor, geoType,limit,skip);
    }

    @Override
    public List<GeoPOD> searchAroundDocuments(ISecuritySession securitySession, String category, String receptor,String geoType,long limit,long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(category, receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("分类<%s>不存在地理感知器：%s。", category, receptor));
        }
        return geosphereLinkService.searchAroundDocuments(securitySession.principal(),geoReceptor,geoType,limit,skip);
    }

    @Override
    public void followReceptor(ISecuritySession securitySession, String category, String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(category, receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("分类<%s>不存在地理感知器：%s。", category, receptor));
        }
         geosphereLinkService.followReceptor(geoReceptor,securitySession.principal());
    }

    @Override
    public void unfollowReceptor(ISecuritySession securitySession, String category, String receptor) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(category, receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("分类<%s>不存在地理感知器：%s。", category, receptor));
        }
         geosphereLinkService.unfollowReceptor(geoReceptor,securitySession.principal());
    }

    @Override
    public List<GeoPOF> pageReceptorFans(ISecuritySession securitySession, String category, String receptor, long limit, long skip) throws CircuitException {
        GeoReceptor geoReceptor = geosphereLinkService.getReceptor(category, receptor);
        if (geoReceptor == null) {
            throw new CircuitException("404", String.format("分类<%s>不存在地理感知器：%s。", category, receptor));
        }
        return geosphereLinkService.pageReceptorFans(securitySession.principal(),geoReceptor,limit,skip);
    }

    @Override
    public List<Channel> listReceptorChannels(ISecuritySession securitySession) throws CircuitException {
        return geosphereLinkService.listReceptorChannels(securitySession.principal());
    }
}
