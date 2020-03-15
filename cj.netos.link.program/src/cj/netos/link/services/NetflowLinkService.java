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
import cj.netos.link.entities.PersonInfo;
import cj.studio.ecm.annotation.CjService;
import cj.ultimate.gson2.com.google.gson.Gson;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "netflowLinkService")
public class NetflowLinkService extends AbstractLinkService implements INetflowLinkService {
    @Override
    public boolean existsChannel(String principal, String channel) {
        ICube cube = cube(principal);
        return cube.tupleCount("channels", String.format("{'tuple.channel':'%s'}", channel)) > 0;
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
                Document.parse(String.format("{'tuple.channel':'%s'}", channel)),
                Document.parse(String.format("{'$set':{'tuple.outGeoSelector':'%s'}}", outGeoSelector)));
    }

    @Override
    public void updateOutPersonSelector(String principal, String channel, String outPersonSelector) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s'}", channel)),
                Document.parse(String.format("{'$set':{'tuple.outPersonSelector':'%s'}}", outPersonSelector)));
    }

    @Override
    public void updateChannelLeading(String principal, String channel, String leading) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s'}", channel)),
                Document.parse(String.format("{'$set':{'tuple.leading':'%s'}}", leading)));
    }

    @Override
    public void updateChanneTitle(String principal, String channel, String title) {
        ICube cube = cube(principal);
        cube.updateDocOne("channels",
                Document.parse(String.format("{'tuple.channel':'%s'}", channel)),
                Document.parse(String.format("{'$set':{'tuple.title':'%s'}}", title)));
    }

    @Override
    public void removeChannel(String principal, String channel) {
        ICube cube = cube(principal);
        cube.deleteDocOne("channels", String.format("{'tuple.channel':'%s'}", channel));
    }

    @Override
    public List<Channel> pageChannel(String principal, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple channels %s where {}", limit, offset, Channel.class.getName());
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
    public Channel getMyChannel(String principal, String channel) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple channels %s where {'tuple.channel':'%s'}", Channel.class.getName(), channel);
        IQuery<Channel> query = cube.createQuery(cjql);
        IDocument<Channel> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public Channel getPersonChannel(String person, String channel) {
        ICube cube = cube(person);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple channels %s where {'tuple.channel':'%s'}", Channel.class.getName(), channel);
        IQuery<Channel> query = cube.createQuery(cjql);
        IDocument<Channel> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public List<Channel> listPersonChannels(String person) {
        ICube cube = cube(person);
        String cjql = String.format("select {'tuple':'*'} from tuple channels %s where {}", Channel.class.getName());
        IQuery<Channel> query = cube.createQuery(cjql);
        List<IDocument<Channel>> docs = query.getResultList();
        List<Channel> list = new ArrayList<>();
        for (IDocument<Channel> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public boolean existsInputPerson(String principal, String channel, String person) {
        ICube cube = cube(principal);
        return cube.tupleCount("input.persons", String.format("{'tuple.channel':'%s','tuple.person':'%s'}", channel, person)) > 0;
    }

    @Override
    public void addInputPerson(String principal, ChannelInputPerson channelInputPerson) {
        ICube cube = cube(principal);
        cube.saveDoc("input.persons", new TupleDocument<>(channelInputPerson));
    }

    @Override
    public void removeInputPerson(String principal, String channel, String person) {
        ICube cube = cube(principal);
        cube.deleteDocOne("input.persons", String.format("{'tuple.channel':'%s','tuple.person':'%s'}", channel, person));
    }

    @Override
    public List<ChannelInputPerson> pageInputPerson(String principal, String channel, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple input.persons %s where {'tuple.channel':'%s'}", limit, offset, ChannelInputPerson.class.getName(), channel);
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
    public List<ChannelInputPerson> listInputPerson(String principal, String channel) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'} from tuple input.persons %s where {'tuple.channel':'%s'}", ChannelInputPerson.class.getName(), channel);
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
    public boolean existsOutputPerson(String principal, String channel, String person) {
        ICube cube = cube(principal);
        return cube.tupleCount("output.persons", String.format("{'tuple.channel':'%s','tuple.person':'%s'}", channel, person)) > 0;
    }

    @Override
    public void addOutputPerson(String principal, ChannelOutputPerson outputPerson) {
        ICube cube = cube(principal);
        cube.saveDoc("output.persons", new TupleDocument<>(outputPerson));
    }

    @Override
    public void removeOutputPerson(String principal, String channel, String person) {
        ICube cube = cube(principal);
        cube.deleteDocOne("output.persons", String.format("{'tuple.channel':'%s','tuple.person':'%s'}", channel, person));
    }

    @Override
    public List<PersonInfo> pageOutputPerson(String principal, String channel, int limit, long offset) {
        return pageOutputPersonOf(principal, channel, limit, offset);
    }

    @Override
    public List<PersonInfo> pageOutputPersonOf(String person, String channel, int limit, long offset) {
        ICube cube = cube(person);
        Channel ch = getPersonChannel(person, channel);
        String cjql = String.format("select {'tuple':'*'} from tuple output.persons %s where {'tuple.channel':'%s'}", ChannelOutputPerson.class.getName(), channel);
        IQuery<ChannelOutputPerson> query = cube.createQuery(cjql);
        List<IDocument<ChannelOutputPerson>> docs = query.getResultList();

        List<String> officials = new ArrayList<>();
        for (IDocument<ChannelOutputPerson> doc : docs) {
            officials.add(doc.tuple().getPerson());
        }
        String json = new Gson().toJson(officials);
        String outPersonSelector = ch.getOutPersonSelector();
        if ("only_select".equals(outPersonSelector)) {
            cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple persons %s where {'tuple.official':{'$in':%s}}", limit, offset, PersonInfo.class.getName(),  json);
            IQuery<PersonInfo> q = cube.createQuery(cjql);
            List<IDocument<PersonInfo>> _docs = q.getResultList();
            List<PersonInfo> persons = new ArrayList<>();
            for (IDocument<PersonInfo> doc : _docs) {
                persons.add(doc.tuple());
            }
            return persons;
        }
        //下面是all_except
        cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple persons %s where {'tuple.official':{'$nin':%s}}", limit, offset, PersonInfo.class.getName(),  json);
        IQuery<PersonInfo> q = cube.createQuery(cjql);
        List<IDocument<PersonInfo>> _docs = q.getResultList();
        List<PersonInfo> persons = new ArrayList<>();
        for (IDocument<PersonInfo> doc : _docs) {
            persons.add(doc.tuple());
        }
        return persons;
    }

    @Override
    public void addPerson(String principal, PersonInfo person) {
        ICube cube = cube(principal);
        cube.saveDoc("persons", new TupleDocument<>(person));
    }

    @Override
    public void removePerson(String principal, String person) {
        ICube cube = cube(principal);
        cube.deleteDocOne("persons", String.format("{'tuple.official':'%s'}", person));
    }
}
