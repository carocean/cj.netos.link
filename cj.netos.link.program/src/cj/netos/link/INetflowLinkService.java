package cj.netos.link;

import cj.netos.link.entities.Channel;
import cj.netos.link.entities.ChannelInputPerson;
import cj.netos.link.entities.ChannelOutputPerson;
import cj.netos.link.entities.PersonInfo;

import java.util.List;

public interface INetflowLinkService {
    boolean existsChannel(String principal, String channel);

    void addChannel(String principal,Channel ch);

    void updateOutGeoSelector(String principal, String channel, String outGeoSelector);

    void updateOutPersonSelector(String principal, String channel, String outPersonSelector);

    void removeChannel(String principal, String channel);

    List<Channel> pageChannel(String principal, int limit, long offset);

    Channel getMyChannel(String principal, String channel);

    boolean existsInputPerson(String principal, String channel, String person);

    void addInputPerson(String principal,ChannelInputPerson channelInputPerson);

    void removeInputPerson(String principal, String channel, String person);

    List<ChannelInputPerson> pageInputPerson(String principal, String channel, int limit, long offset);

    List<ChannelInputPerson> listInputPerson(String principal, String channel);

    boolean existsOutputPerson(String principal, String channel, String person);

    void addOutputPerson(String principal,ChannelOutputPerson outputPerson);

    void removeOutputPerson(String principal, String channel, String person);

    List<PersonInfo> pageOutputPerson(String principal, String channel, int limit, long offset);

    void updateChannelLeading(String principal, String channel, String leading);

    void updateChanneTitle(String principal, String channel, String title);

    boolean existsPerson(String principal, String person);

    void addPerson(String principal, PersonInfo person);

    void removePerson(String principal, String person);

    Channel getPersonChannel(String person, String channel);

    List<Channel> listPersonChannels(String person);

    List<PersonInfo> pageOutputPersonOf(String person, String channel, int limit, long offset);

    List<PersonInfo> pageInputPersonOf(String person, String channel, int limit, long offset);

    List<Channel> getAllMyChannel(String principal, long ctime);

}
