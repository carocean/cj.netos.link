package cj.netos.link.services;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.link.AbstractLinkService;
import cj.netos.link.IChatroomService;
import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;
import cj.studio.ecm.annotation.CjService;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CjService(name = "chatroomService")
public class ChatroomService extends AbstractLinkService implements IChatroomService {
    private String _getPersonByRoom(String room) {
        int pos = room.indexOf("/");
        String person = room.substring(0, pos);
        return person;
    }

    @Override
    public Chatroom getRoom(String principal, String room) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple chat.rooms %s where {'tuple.room':'%s'}", Chatroom.class.getName(), room);
        IQuery<Chatroom> query = cube.createQuery(cjql);
        IDocument<Chatroom> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public void updateSeal(String principal,String room, boolean isSeal) {
        ICube cube = cube(principal);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.isSeal':%s}}", isSeal)));
    }

    @Override
    public void addRoom(String principal, Chatroom chatroom) {
        ICube cube = cube(principal);
        cube.saveDoc("chat.rooms", new TupleDocument<>(chatroom));

        RoomMember member = new RoomMember();
        member.setActor("creator");
        member.setAtime(System.currentTimeMillis());
        member.setNickName(null);
        member.setPerson(chatroom.getCreator());
        member.setRoom(chatroom.getRoom());
        cube.saveDoc("chat.members", new TupleDocument<>(member));
    }
//
//    @Override
//    public void removeRoom(String principal, String room) {
//        ICube cube = cube(principal);
//        cube.deleteDocOne("chat.rooms", String.format("{'tuple.room':'%s'}", room));
//    }

    @Override
    public void flagDeletedRoom(String principal, String room) {
        ICube cube = cube(principal);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.flag':1}}"))
        );
    }

    @Override
    public List<Chatroom> pageRoom(String principal, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple chat.rooms %s where {'tuple.creator':'%s','tuple.flag':{'$ne':1}}", limit, offset, Chatroom.class.getName(), principal);
        IQuery<Chatroom> query = cube.createQuery(cjql);
        List<IDocument<Chatroom>> docs = query.getResultList();
        List<Chatroom> chatrooms = new ArrayList<>();
        for (IDocument<Chatroom> doc : docs) {
            chatrooms.add(doc.tuple());
        }
        return chatrooms;
    }

    @Override
    public void addMember(String principal, RoomMember member) {
        ICube cube = cube(principal);
        cube.saveDoc("chat.members", new TupleDocument<>(member));
    }

    @Override
    public long countMember(String creator, String room) {
        ICube cube = cube(creator);
        return cube.tupleCount("chat.members", String.format("{'tuple.room':'%s'}", room));
    }

    @Override
    public void emptyMember(String creator, String room) {
        ICube cube = cube(creator);
        cube.deleteDocs("chat.members", String.format("{'tuple.room':'%s'}", room));
    }

    @Override
    public RoomMember getRoomMember(Chatroom chatroom, String principal) {
        ICube cube = cube(chatroom.getCreator());
        String cjql = String.format("select {'tuple':'*'}.limit(1) from tuple chat.members %s where {'tuple.room':'%s','tuple.person':'%s'}  ", RoomMember.class.getName(), chatroom.getRoom(), principal);
        IQuery<RoomMember> query = cube.createQuery(cjql);
        IDocument<RoomMember> document = query.getSingleResult();
        if (document == null) {
            return null;
        }
        return document.tuple();
    }

    @Override
    public boolean existsMember(String principal, String room, String person) {
        ICube cube = cube(principal);
        return cube.tupleCount("chat.members", String.format("{'tuple.person':'%s','tuple.room':'%s'}", person, room)) > 0;
    }

    @Override
    public void removeMember(String roomCreator, String room, String person) {
        ICube cube = cube(roomCreator);
//        cube.deleteDocOne("chat.members", String.format("{'tuple.person':'%s','tuple.room':'%s'}", person, room));
        cube.updateDocOne("chat.members",
                Document.parse(String.format("{'tuple.person':'%s','tuple.room':'%s'}", person, room)),
                Document.parse(String.format("{'$set':{'tuple.flag':1}}"))
        );
    }

    @Override
    public List<RoomMember> pageRoomMember(String principal, String room, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple chat.members %s where {'tuple.room':'%s','tuple.flag':{'$ne':1}}  ", limit, offset, RoomMember.class.getName(), room);
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
    public List<String> listFlagRoomMember(String roomCreator, String room) {
        ICube cube = cube(roomCreator);
        String cjql = String.format("select {'tuple.person':1} from tuple chat.members %s where {'tuple.room':'%s','tuple.flag':1}  ", HashMap.class.getName(), room);
        IQuery<Map<String,String>> query = cube.createQuery(cjql);
        List<IDocument<Map<String,String>>> docs = query.getResultList();
        List<String> members = new ArrayList<>();
        for (IDocument<Map<String,String>> doc : docs) {
            members.add(doc.tuple().get("person"));
        }
        return members;
    }

    @Override
    public List<RoomMember> getActorRoomMembers(String principal, String room, String actor) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'} from tuple chat.members %s where {'tuple.room':'%s','tuple.flag':{'$ne':1},'tuple.actor':'%s'}", RoomMember.class.getName(), room, actor);
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
    public void updateNickName(String creator, String room, String person, String nickName) {
        ICube cube = cube(creator);
        cube.updateDocOne("chat.members",
                Document.parse(String.format("{'tuple.room':'%s','tuple.person':'%s'}", room, person)),
                Document.parse(String.format("{'$set':{'tuple.nickName':'%s'}}", nickName)));
    }

    @Override
    public void setShowNick(Chatroom chatroom, String member, boolean isShowNick) {
        ICube cube = cube(chatroom.getCreator());
        cube.updateDocOne("chat.members",
                Document.parse(String.format("{'tuple.room':'%s','tuple.person':'%s'}", chatroom.getRoom(), member)),
                Document.parse(String.format("{'$set':{'tuple.isShowNick':%s}}", isShowNick)));
    }

    @Override
    public void updateLeading(String principal, String room, String leading) {
        ICube cube = cube(principal);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.leading':'%s'}}", leading)));
    }

    @Override
    public void updateTitle(String principal, String room, String title) {
        ICube cube = cube(principal);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.title':'%s'}}", title)));
    }

    @Override
    public void updateBackground(String creator, String room, String background) {
        ICube cube = cube(creator);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.background':'%s'}}", background)));
    }

    @Override
    public void updateRoomForeground(String creator, String room, boolean isForegroundWhite) {
        ICube cube = cube(creator);
        cube.updateDocOne("chat.rooms",
                Document.parse(String.format("{'tuple.room':'%s'}", room)),
                Document.parse(String.format("{'$set':{'tuple.isForegroundWhite':'%s'}}", isForegroundWhite)));
    }

    @Override
    public void updateFlag(String creator, String room, String person,int flag) {
        ICube cube = cube(creator);
        cube.updateDocOne("chat.members",
                Document.parse(String.format("{'tuple.room':'%s','tuple.person':'%s'}", room,person)),
                Document.parse(String.format("{'$set':{'tuple.flag':%s}}",flag)));
    }

    @Override
    public long totalRoomMember(String roomCreator, String room) {
        ICube cube = cube(roomCreator);
        return cube.tupleCount("chat.members",String.format("{'tuple.room':'%s','tuple.flag':0}",room));
    }

    @Override
    public void addNotice(String principal, RoomNotice roomNotice) {
        ICube cube = cube(principal);
        cube.saveDoc("chat.notices", new TupleDocument<>(roomNotice));
    }

    @Override
    public RoomNotice getNewestNotice(String principal, String room) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(1).sort({'tuple.ctime':-1}) from tuple chat.notices %s where {'tuple.room':'%s'}", RoomNotice.class.getName(), room);
        IQuery<RoomNotice> query = cube.createQuery(cjql);
        IDocument<RoomNotice> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }

    @Override
    public List<RoomNotice> pageNotice(String principal, String room, int limit, long offset) {
        ICube cube = cube(principal);
        String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s).sort({'tuple.ctime':-1}) from tuple chat.notices %s where {'tuple.room':'%s'}", limit, offset, RoomNotice.class.getName(), room);
        IQuery<RoomNotice> query = cube.createQuery(cjql);
        List<IDocument<RoomNotice>> docs = query.getResultList();
        List<RoomNotice> list = new ArrayList<>();
        for (IDocument<RoomNotice> doc : docs) {
            list.add(doc.tuple());
        }
        return list;
    }

    @Override
    public RoomMember getMember(String principal, String room, String person) {
        ICube cube = cube(principal);
        String cjql = "select {'tuple':'*'}.limit(1) from tuple chat.members ?(clazz) where {'tuple.room':'?(room)','tuple.person':'?(person)'}";
        IQuery<RoomMember> query = cube.createQuery(cjql);
        query.setParameter("clazz", RoomMember.class.getName());
        query.setParameter("room", room);
        query.setParameter("person", person);
        IDocument<RoomMember> doc = query.getSingleResult();
        if (doc == null) {
            return null;
        }
        return doc.tuple();
    }
}
