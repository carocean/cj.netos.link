package cj.netos.link.ports;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.ChannelInputPerson;
import cj.netos.link.entities.ChannelOutputPerson;
import cj.netos.link.entities.PersonInfo;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.PKeyInRequest;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;
import java.util.Map;

@CjOpenports(usage = "网流个人自助服务")
public interface INetflowLinkPorts extends IOpenportService {
    @CjOpenport(usage = "创建管道")
    void createChannel(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号", name = "channel") String channel,
            @CjOpenportParameter(usage = "来源管道，它如果与id相等表示该管道就是源头管道。别人依据它创建的管道需要将本管道的id赋给创建的管道的origin", name = "origin") String origin,
            @CjOpenportParameter(usage = "管道显示名", name = "title") String title,
            @CjOpenportParameter(usage = "管道图标", name = "leading") String leading,
            @CjOpenportParameter(usage = "公众选择策略：only_select, all_except,", name = "outPersonSelector", defaultValue = "only_select") String outPersonSelector,
            @CjOpenportParameter(usage = "是否选择出口地圈,true为是，false为否", name = "outGeoSelector", defaultValue = "true") String outGeoSelector
    ) throws CircuitException;

    @CjOpenport(usage = "更新管道输出地圈的策略")
    void updateOutGeoSelector(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "是否选择出口地圈,true为是，false为否", name = "outGeoSelector", defaultValue = "true") String outGeoSelector
    ) throws CircuitException;

    @CjOpenport(usage = "更新管道输出用户的策略")
    void updateOutPersonSelector(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "公众选择策略：only_select, all_except,", name = "outPersonSelector", defaultValue = "only_select") String outPersonSelector
    ) throws CircuitException;

    @CjOpenport(usage = "更新管道图标，只能更新访问者创建的管道")
    void updateChannelLeading(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "图标", name = "leading") String leading
    ) throws CircuitException;

    @CjOpenport(usage = "更新管道显示名，只能更新访问者创建的管道")
    void updateChanneTitle(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "显示名", name = "title") String title
    ) throws CircuitException;

    @CjOpenport(usage = "移除管道")
    void removeChannel(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel
    ) throws CircuitException;

    @CjOpenport(usage = "列出当前访问者的管道")
    List<Channel> pageChannel(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
            @CjOpenportParameter(usage = "当前位置", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "获取我的管道")
    Channel getMyChannel(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel
    ) throws CircuitException;

    @CjOpenport(usage = "获取公众管道")
    Channel getPersonChannel(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "公众号。", name = "person") String person,
            @CjOpenportParameter(usage = "管道号。", name = "channel") String channel
    ) throws CircuitException;

    @CjOpenport(usage = "加入公众到管道的输入端")
    void addInputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "上游公众", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "从输入中移除")
    void removeInputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "上游公众", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "分页输入公众")
    List<ChannelInputPerson> pageInputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
            @CjOpenportParameter(usage = "当前位置", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "列出所有输入公众")
    List<ChannelInputPerson> listInputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "上游公众", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "为管道添加输出公众")
    void addOutputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "上游公众", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "移除输出公众")
    void removeOutputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "上游公众", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "分页输出公众")
    List<ChannelOutputPerson> pageOutputPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "所在的管道号。", name = "channel") String channel,
            @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
            @CjOpenportParameter(usage = "当前位置", name = "offset") long offset
    ) throws CircuitException;

    @CjOpenport(usage = "添加公众", command = "post")
    void addPerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "公众信息。", name = "person", in = PKeyInRequest.content) PersonInfo person
    ) throws CircuitException;

    @CjOpenport(usage = "移除公众")
    void removePerson(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "公号。", name = "person") String person
    ) throws CircuitException;

}
