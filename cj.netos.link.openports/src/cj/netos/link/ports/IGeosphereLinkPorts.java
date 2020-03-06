package cj.netos.link.ports;

import cj.netos.link.entities.geo.GeoObjectReponse;
import cj.netos.link.entities.geo.Location;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.PKeyInRequest;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

/*
本项目实现地圈的相交关系查询等功能，位置的更新由项目location实现；地理信息的入库及管理由document实现；
 */
@CjOpenports(usage = "地圈个人自助服务")
public interface IGeosphereLinkPorts extends IOpenportService {
    @CjOpenport(usage = "查询在圆内的所有行人", command = "post")
    List<GeoObjectReponse> circleFindPedestrian(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "位置", name = "location", in = PKeyInRequest.content) Location point,
            @CjOpenportParameter(usage = "半径，单位为米", name = "radius") double radius
    ) throws CircuitException;
}
