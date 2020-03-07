package cj.netos.link.ports;

import cj.netos.link.INetflowLinkService;
import cj.netos.link.entities.Channel;
import cj.netos.link.entities.ChannelInputPerson;
import cj.netos.link.entities.ChannelOutputPerson;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/netflow/self.service")
public class NetflowLinkPorts implements INetflowLinkPorts {
    @CjServiceRef
    INetflowLinkService netflowLinkService;

    @Override
    public void createChannel(ISecuritySession securitySession, String channel, String origin, String title, String leading, String outPersonSelector, String outGeoSelector) throws CircuitException {
        if (netflowLinkService.existsChannel(securitySession.principal(), origin)) {
            throw new CircuitException("500", "已存在管道");
        }
        Channel ch = new Channel();
        ch.setChannel(channel);
        ch.setOrigin(origin);
        ch.setCreator(securitySession.principal());
        ch.setCtime(System.currentTimeMillis());
        ch.setLeading(leading);
        ch.setOutPersonSelector(outPersonSelector);
        ch.setOutGeoSelector(outGeoSelector);
        ch.setTitle(title);
        netflowLinkService.addChannel(securitySession.principal(), ch);
    }

    @Override
    public void updateOutGeoSelector(ISecuritySession securitySession, String channel, String outGeoSelector) throws CircuitException {
        netflowLinkService.updateOutGeoSelector(securitySession.principal(), channel, outGeoSelector);
    }

    @Override
    public void updateOutPersonSelector(ISecuritySession securitySession, String channel, String outPersonSelector) throws CircuitException {
        netflowLinkService.updateOutPersonSelector(securitySession.principal(), channel, outPersonSelector);
    }

    @Override
    public void updateChannelLeading(ISecuritySession securitySession, String channel, String leading) throws CircuitException {
        netflowLinkService.updateChannelLeading(securitySession.principal(), channel, leading);
    }

    @Override
    public void updateChanneTitle(ISecuritySession securitySession, String channel, String title) throws CircuitException {
        netflowLinkService.updateChanneTitle(securitySession.principal(), channel, title);
    }

    @Override
    public void removeChannel(ISecuritySession securitySession, String channel) throws CircuitException {
        netflowLinkService.removeChannel(securitySession.principal(), channel);
    }

    @Override
    public List<Channel> pageChannel(ISecuritySession securitySession, int limit, long offset) throws CircuitException {
        return netflowLinkService.pageChannel(securitySession.principal(), limit, offset);
    }

    @Override
    public Channel getChannel(ISecuritySession securitySession, String channel) throws CircuitException {
        String owner = channel.substring(0, channel.indexOf("/"));
        return netflowLinkService.getChannel(owner, channel);
    }

    @Override
    public void addInputPerson(ISecuritySession securitySession, String channel, String person) throws CircuitException {
        if (!netflowLinkService.existsChannel(securitySession.principal(), channel)) {
            throw new CircuitException("404", "管道不存在");
        }
        ChannelInputPerson channelInputPerson = new ChannelInputPerson();
        channelInputPerson.setAtime(System.currentTimeMillis());
        channelInputPerson.setChannel(channel);
        channelInputPerson.setPerson(person);
        netflowLinkService.addInputPerson(securitySession.principal(), channelInputPerson);
    }

    @Override
    public void removeInputPerson(ISecuritySession securitySession, String channel, String person) throws CircuitException {
        netflowLinkService.removeInputPerson(securitySession.principal(), channel, person);
    }

    @Override
    public List<ChannelInputPerson> pageInputPerson(ISecuritySession securitySession, String channel, int limit, long offset) throws CircuitException {
        String owner = channel.substring(0, channel.indexOf("/"));
        return netflowLinkService.pageInputPerson(owner, channel, limit, offset);
    }

    @Override
    public List<ChannelInputPerson> listInputPerson(ISecuritySession securitySession, String channel, String person) throws CircuitException {
        String owner = channel.substring(0, channel.indexOf("/"));
        return netflowLinkService.listInputPerson(owner, channel);
    }

    @Override
    public void addOutputPerson(ISecuritySession securitySession, String channel, String person) throws CircuitException {
        if (!netflowLinkService.existsChannel(securitySession.principal(), channel)) {
            throw new CircuitException("404", "管道不存在");
        }
        if (netflowLinkService.existsOutputPerson(securitySession.principal(), channel, person)) {
            throw new CircuitException("500", "公众已在输出端");
        }
        ChannelOutputPerson outputPerson = new ChannelOutputPerson();
        outputPerson.setAtime(System.currentTimeMillis());
        outputPerson.setChannel(channel);
        outputPerson.setPerson(person);
        netflowLinkService.addOutputPerson(securitySession.principal(), outputPerson);
    }

    @Override
    public void removeOutputPerson(ISecuritySession securitySession, String channel, String person) throws CircuitException {
        netflowLinkService.removeOutputPerson(securitySession.principal(), channel, person);
    }

    @Override
    public List<ChannelOutputPerson> pageOutputPerson(ISecuritySession securitySession, String channel, int limit, long offset) throws CircuitException {
        String owner = channel.substring(0, channel.indexOf("/"));
        return netflowLinkService.pageOutputPerson(owner, channel, limit, offset);
    }
}
