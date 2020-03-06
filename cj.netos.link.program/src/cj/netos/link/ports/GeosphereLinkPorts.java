package cj.netos.link.ports;

import cj.netos.link.entities.geo.GeoObjectReponse;
import cj.netos.link.entities.geo.Location;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;
@CjService(name = "/geosphere/self.service")
public class GeosphereLinkPorts implements IGeosphereLinkPorts {
    @Override
    public List<GeoObjectReponse> circleFindPedestrian(ISecuritySession securitySession, Location point, double radius) throws CircuitException {
        return null;
    }
}
