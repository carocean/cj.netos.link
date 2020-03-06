package cj.netos.link;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.ChannelInputPerson;
import cj.netos.link.entities.ChannelOutputPerson;

import java.util.List;

public interface INetflowLinkService {
    boolean existsChannel(String principal, String channel);

    void addChannel(String principal,Channel ch);

    void updateOutGeoSelector(String principal, String channel, String outGeoSelector);

    void updateOutPersonSelector(String principal, String channel, String outPersonSelector);

    void removeChannel(String principal, String channel);

    List<Channel> pageChannel(String principal, int limit, long offset);

    Channel getChannel(String principal, String channel);

    boolean existsInputPerson(String principal, String onchannel, String person, String channel);

    void addInputPerson(String principal,ChannelInputPerson channelInputPerson);

    void removeInputPerson(String principal, String onchannel, String person, String channel);

    List<ChannelInputPerson> pageInputPerson(String principal, String onchannel, int limit, long offset);

    List<ChannelInputPerson> listInputPerson(String principal, String onchannel);

    boolean existsOutputPerson(String principal, String onchannel, String person);

    void addOutputPerson(String principal,ChannelOutputPerson outputPerson);

    void removeOutputPerson(String principal, String onchannel, String person);

    List<ChannelOutputPerson> pageOutputPerson(String principal, String onchannel, int limit, long offset);

    void updateChannelLeading(String principal, String channel, String leading);

    void updateChanneTitle(String principal, String channel, String title);
}
