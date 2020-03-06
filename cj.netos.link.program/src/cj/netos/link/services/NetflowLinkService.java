package cj.netos.link.services;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.link.AbstractLinkService;
import cj.netos.link.INetflowLinkService;
import cj.netos.link.entities.Channel;
import cj.netos.link.entities.ChannelInputPerson;
import cj.netos.link.entities.ChannelOutputPerson;
import cj.netos.link.entities.Chatroom;
import cj.studio.ecm.annotation.CjService;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "netflowLinkService")
public class NetflowLinkService extends AbstractLinkService implements INetflowLinkService {
    @Override
    public boolean existsChannel(String principal, String channel) {
        ICube cube = cube(principal);
        return cube.tupleCount("channels", String.format("{'tuple.creator':'%s','tuple.channel':'%s'}", principal, channel)) > 0;
    }

    @Override
    public void addChannel(String principal, Channel ch) {
        ICube cube = cube(principal);
        cube.saveDoc("channels", new TupleDocument<>(ch));
    }

    @Override
    public void updateOutGeoSelector(String principal, String channel, String outGeoSelector) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s','tuple.creator':'%s'}", channel, principal)),
                Document.parse(String.format("{'$set':{'tuple.outGeoSelector':'%s'}}", outGeoSelector)));
    }

    @Override
    public void updateOutPersonSelector(String principal, String channel, String outPersonSelector) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s','tuple.creator':'%s'}", channel, principal)),
                Document.parse(String.format("{'$set':{'tuple.outPersonSelector':'%s'}}", outPersonSelector)));
    }

    @Override
    public void updateChannelLeading(String principal, String channel, String leading) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s','tuple.creator':'%s'}", channel, principal)),
                Document.parse(String.format("{'$set':{'tuple.leading':'%s'}}", leading)));
    }

    @Override
    public void updateChanneTitle(String principal, String channel, String title) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s','tuple.creator':'%s'}", channel, principal)),
                Document.parse(String.format("{'$set':{'tuple.title':'%s'}}", title)));
    }

    @Override
    public void removeChannel(String principal, String channel) {
        ICube cube = cube(principal);
        cube.deleteDocOne("channels", String.format("{'tuple.creator':'%s','tuple.channel':'%s'}", principal, channel));
    }

    @Override
    public List<Channel> pageChannel(String principal, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple channels %s where {'tuple.creator':'%s'}", limit, offset, Channel.class.getName(), principal);
        IQuery<Channel> query = cube.createQuery(cjql);
        List<IDocument<Channel>> docs = query.getResultList();
        List<Channel> channels = new ArrayList<>();
        for (IDocument<Channel> doc : docs) {
            channels.add(doc.tuple()
            );
        }
        return channels;
    }

    @Override
    public Channel getChannel(String principal, String channel) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple channels %s where {'tuple.creator':'%s','tuple.channel':'%s'}", Channel.class.getName(), principal, channel);
        IQuery<Channel> query = cube.createQuery(cjql);
        IDocument<Channel> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public boolean existsInputPerson(String principal, String onchannel, String person, String channel) {
        ICube cube = cube(principal);
        return cube.tupleCount("input.persons", String.format("{'tuple.onchannel':'%s','tuple.person':'%s','tuple.channel':'%s'}", onchannel, person, channel)) > 0;
    }

    @Override
    public void addInputPerson(String principal, ChannelInputPerson channelInputPerson) {
        ICube cube = cube(principal);
        cube.saveDoc("input.persons", new TupleDocument<>(channelInputPerson));
    }

    @Override
    public void removeInputPerson(String principal, String onchannel, String person, String channel) {
        ICube cube = cube(principal);
        cube.deleteDocOne("input.persons", String.format("{'tuple.onchannel':'%s','tuple.person':'%s','tuple.channel':'%s'}", onchannel, person, channel));
    }

    @Override
    public List<ChannelInputPerson> pageInputPerson(String principal, String onchannel, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple input.persons %s where {'tuple.onchannel':'%s'}", limit, offset, ChannelInputPerson.class.getName(), onchannel);
        IQuery<ChannelInputPerson> query = cube.createQuery(cjql);
        List<IDocument<ChannelInputPerson>> docs = query.getResultList();
        List<ChannelInputPerson> inputPersonList = new ArrayList<>();
        for (IDocument<ChannelInputPerson> doc : docs) {
            inputPersonList.add(doc.tuple()
            );
        }
        return inputPersonList;
    }

    @Override
    public List<ChannelInputPerson> listInputPerson(String principal, String onchannel) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'} from tuple input.persons %s where {'tuple.onchannel':'%s'}", ChannelInputPerson.class.getName(), onchannel);
        IQuery<ChannelInputPerson> query = cube.createQuery(cjql);
        List<IDocument<ChannelInputPerson>> docs = query.getResultList();
        List<ChannelInputPerson> inputPersonList = new ArrayList<>();
        for (IDocument<ChannelInputPerson> doc : docs) {
            inputPersonList.add(doc.tuple()
            );
        }
        return inputPersonList;
    }

    @Override
    public boolean existsOutputPerson(String principal, String onchannel, String person) {
        ICube cube = cube(principal);
        return cube.tupleCount("output.persons", String.format("{'tuple.onchannel':'%s','tuple.person':'%s'}", onchannel, person)) > 0;
    }

    @Override
    public void addOutputPerson(String principal, ChannelOutputPerson outputPerson) {
        ICube cube = cube(principal);
        cube.saveDoc("output.persons", new TupleDocument<>(outputPerson));
    }

    @Override
    public void removeOutputPerson(String principal, String onchannel, String person) {
        ICube cube = cube(principal);
        cube.deleteDocOne("output.persons", String.format("{'tuple.onchannel':'%s','tuple.person':'%s'}", onchannel, person));
    }

    @Override
    public List<ChannelOutputPerson> pageOutputPerson(String principal, String onchannel, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple output.persons %s where {'tuple.onchannel':'%s'}", limit, offset, ChannelOutputPerson.class.getName(), onchannel);
        IQuery<ChannelOutputPerson> query = cube.createQuery(cjql);
        List<IDocument<ChannelOutputPerson>> docs = query.getResultList();
        List<ChannelOutputPerson> outputPersonList = new ArrayList<>();
        for (IDocument<ChannelOutputPerson> doc : docs) {
            outputPersonList.add(doc.tuple()
            );
        }
        return outputPersonList;
    }
}
