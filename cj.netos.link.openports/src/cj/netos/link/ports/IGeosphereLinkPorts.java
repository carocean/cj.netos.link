package cj.netos.link.ports;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.geo.GeoPOD;
import cj.netos.link.entities.geo.GeoPOF;
import cj.netos.link.entities.geo.GeoPOI;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

/*
本项目实现地圈的相交关系查询等功能，位置的更新由项目location实现；地理信息的入库及管理由document实现；
 */
@CjOpenports(usage = "地圈个人自助服务")
public interface IGeosphereLinkPorts extends IOpenportService {

    @CjOpenport(usage = "查询指定感知器半径内的感知器，并计算离我的距离")
    List<GeoPOI> searchAroundReceptors(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "感知器所属分类", name = "category") String category,
            @CjOpenportParameter(usage = "感知器标识", name = "receptor") String receptor,
            @CjOpenportParameter(usage = "要搜索的感知器类别，为空是所有分类，格式为：类别1｜类别2｜类别3", name = "geoType") String geoType,
            @CjOpenportParameter(usage = "分页大小。注意：在多分类查询情况下，是每个分类查询出limit个文档", name = "limit", defaultValue = "100") long limit,
            @CjOpenportParameter(usage = "偏移", name = "offset", defaultValue = "0") long skip
    ) throws CircuitException;

    @CjOpenport(usage = "查询指定感知器半径内的活动，并计算离我的距离")
    List<GeoPOD> searchAroundDocuments(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "感知器所属分类", name = "category") String category,
            @CjOpenportParameter(usage = "感知器标识", name = "receptor") String receptor,
            @CjOpenportParameter(usage = "要搜索的感知器类别，为空是所有分类，格式为：类别1｜类别2｜类别3", name = "geoType") String geoType,
            @CjOpenportParameter(usage = "分页大小，注意：在多分类查询情况下，是每个分类查询出limit个文档", name = "limit", defaultValue = "100") long limit,
            @CjOpenportParameter(usage = "偏移", name = "offset", defaultValue = "0") long skip
    ) throws CircuitException;

    @CjOpenport(usage = "我关注指定的感知器")
    void followReceptor(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "感知器所属分类", name = "category") String category,
            @CjOpenportParameter(usage = "感知器标识", name = "receptor") String receptor
    ) throws CircuitException;

    @CjOpenport(usage = "我不再关注指定的感知器")
    void unfollowReceptor(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "感知器所属分类", name = "category") String category,
            @CjOpenportParameter(usage = "感知器标识", name = "receptor") String receptor
    ) throws CircuitException;

    @CjOpenport(usage = "分页指定感知器的粉丝，并计算离我的距离")
    List<GeoPOF> pageReceptorFans(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "感知器所属分类", name = "category") String category,
            @CjOpenportParameter(usage = "感知器标识", name = "receptor") String receptor,
            @CjOpenportParameter(usage = "分页大小", name = "limit") long limit,
            @CjOpenportParameter(usage = "当前记录位置", name = "skip") long skip
    ) throws CircuitException;

    @CjOpenport(usage = "分页指定感知器的网流管道，只有moveableSelf类型的感知器能接收网流管道输入")
    List<Channel> listReceptorChannels(
            ISecuritySession securitySession
    ) throws CircuitException;
}
