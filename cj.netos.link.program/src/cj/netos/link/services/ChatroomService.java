package cj.netos.link.services;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.lns.chip.sos.disk.INetDisk;
import cj.netos.link.IChatroomService;
import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "chatroomService")
public class ChatroomService extends AbstractLinkService implements IChatroomService {
    private String _getPersonByRoom(String room) {
        int pos = room.indexOf("/");
        String person = room.substring(0, pos);
        return person;
    }

    @Override
    public Chatroom getRoom(String room) {
        ICube cube = cube(_getPersonByRoom(room));
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple chatrooms %s where {'tuple.room':'%s'}", Chatroom.class.getName(), room);
        IQuery<Chatroom> query = cube.createQuery(cjql);
        IDocument<Chatroom> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public void addRoom(Chatroom chatroom) {
        ICube cube = cube(chatroom.getCreator());
        cube.saveDoc("chatrooms", new TupleDocument<>(chatroom));

        RoomMember member = new RoomMember();
        member.setActor("creator");
        member.setOwner(chatroom.getCreator());
        member.setAtime(System.currentTimeMillis());
        member.setNickName(null);
        member.setPerson(chatroom.getCreator());
        member.setRoom(chatroom.getRoom());
        cube.saveDoc("members", new TupleDocument<>(member));
    }

    @Override
    public void removeRoom(String room) {
        ICube cube = cube(_getPersonByRoom(room));
        cube.deleteDocOne("chatrooms", String.format("{'tuple.room':'%s'}", room));
    }

    @Override
    public List<Chatroom> pageRoom(String person, int limit, long offset) {
        ICube cube = cube(person);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple chatrooms %s where {'tuple.creator':'%s'}", limit, offset, Chatroom.class.getName(), person);
        IQuery<Chatroom> query = cube.createQuery(cjql);
        List<IDocument<Chatroom>> docs = query.getResultList();
        List<Chatroom> chatrooms = new ArrayList<>();
        for (IDocument<Chatroom> doc : docs) {
            chatrooms.add(doc.tuple());
        }
        return chatrooms;
    }

    @Override
    public void addMember(RoomMember member) {
        ICube cube = cube(member.getOwner());
        cube.saveDoc("members", new TupleDocument<>(member));
    }

    @Override
    public boolean existsMember(String room, String person) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        return cube.tupleCount("members", String.format("{'tuple.person':'%s','tuple.room':'%s'}", person, room)) > 0;
    }

    @Override
    public void removeMember(String room, String person) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        cube.deleteDocOne("members", String.format("{'tuple.person':'%s','tuple.room':'%s'}", person, room));
    }

    @Override
    public List<RoomMember> pageAnyRoomMember(String room, int limit, long offset) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple members %s where {'tuple.room':'%s'}  ", limit, offset, RoomMember.class.getName(), room);
        IQuery<RoomMember> query = cube.createQuery(cjql);
        List<IDocument<RoomMember>> docs = query.getResultList();
        List<RoomMember> members = new ArrayList<>();
        for (IDocument<RoomMember> doc : docs) {
            members.add(doc.tuple()
            );
        }
        return members;
    }

    @Override
    public List<RoomMember> getActorRoomMembers(String room, String actor) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        String cjql = String.format("select {'tuple':'*'} from tuple members %s where {'tuple.room':'%s','tuple.actor':'%s'}", RoomMember.class.getName(), room, actor);
        IQuery<RoomMember> query = cube.createQuery(cjql);
        List<IDocument<RoomMember>> docs = query.getResultList();
        List<RoomMember> members = new ArrayList<>();
        for (IDocument<RoomMember> doc : docs) {
            members.add(doc.tuple()
            );
        }
        return members;
    }

    @Override
    public void updateNickName(String room, String person, String nickName) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        cube.updateDocOne("members",
                Document.parse(String.format("{'tuple.room':'%s','tuple.person':'%s'}", room, person)),
                Document.parse(String.format("{'$set':{'tuple.nickName':'%s'}}", nickName)));
    }

    @Override
    public void addNotice(RoomNotice roomNotice) {
        Chatroom chatroom = getRoom(roomNotice.getRoom());
        ICube cube = cube(chatroom.getCreator());
        cube.saveDoc("notices", new TupleDocument<>(roomNotice));
    }

    @Override
    public RoomNotice getNewestNotice(String room) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        String cjql = String.format("select {'tuple':'*'}.limit(1).sort({'tuple.ctime':-1}) from tuple notices %s where {'tuple.room':'%s'}",RoomNotice.class.getName(),room );
        IQuery<RoomNotice> query = cube.createQuery(cjql);
        IDocument<RoomNotice> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public List<RoomNotice> pageNotice(String room, int limit, long offset) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s).sort({'tuple.ctime':-1}) from tuple notices %s where {'tuple.room':'%s'}", limit, offset, RoomNotice.class.getName(), room);
        IQuery<RoomNotice> query = cube.createQuery(cjql);
        List<IDocument<RoomNotice>> docs=query.getResultList();
        List<RoomNotice> list = new ArrayList<>();
        for (IDocument<RoomNotice> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public RoomMember getMember(String room, String person) {
        Chatroom chatroom = getRoom(room);
        ICube cube = cube(chatroom.getCreator());
        String cjql = "select {'tuple':'*'}.limit(1) from tuple members ?(clazz) where {'tuple.room':'?(room)','tuple.person':'?(person)'}";
        IQuery<RoomMember> query = cube.createQuery(cjql);
        query.setParameter("clazz",RoomMember.class.getName());
        query.setParameter("room", room);
        query.setParameter("person", person);
        IDocument<RoomMember> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }
}
