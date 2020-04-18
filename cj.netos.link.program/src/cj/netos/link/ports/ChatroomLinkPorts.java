package cj.netos.link.ports;

import cj.netos.link.IChatroomService;
import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.ArrayList;
import java.util.List;

@CjService(name = "/chatroom/self.service")
public class ChatroomLinkPorts implements IChatroomLinkPorts {
    @CjServiceRef
    IChatroomService chatroomService;

    @Override
    public void createRoom(ISecuritySession securitySession, String id, String title, String leading, String microsite) throws CircuitException {
        if (chatroomService.getRoom(securitySession.principal(), id) != null) {
            throw new CircuitException("500", "已存在聊天室");
        }
        Chatroom chatroom = new Chatroom();
        chatroom.setRoom(id);
        chatroom.setCreator(securitySession.principal());
        chatroom.setCtime(System.currentTimeMillis());
        chatroom.setLeading(leading);
        chatroom.setMicrosite(microsite);
        chatroom.setTitle(title);
        chatroomService.addRoom(securitySession.principal(), chatroom);
    }

    @Override
    public void removeRoom(ISecuritySession securitySession, String room) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            return;
        }
        if (chatroom.getCreator().equals(securitySession.principal())) {
            chatroomService.emptyMember(chatroom.getCreator(), room);
        } else {
            //不是创建者说明是我加入的群，则将我从成员中移除
            chatroomService.removeMember(chatroom.getCreator(), room, securitySession.principal());
        }
        chatroomService.removeRoom(securitySession.principal(), room);
    }

    @Override
    public Chatroom getRoom(ISecuritySession securitySession, String room) throws CircuitException {
        return chatroomService.getRoom(securitySession.principal(), room);
    }

    @Override
    public Chatroom getRoomOfPerson(ISecuritySession securitySession, String room, String person) throws CircuitException {
        return chatroomService.getRoom(person, room);
    }

    @Override
    public List<Chatroom> pageRoom(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        return chatroomService.pageRoom(securitySession.principal(), limit, offset);
    }

    @Override
    public void addMember(ISecuritySession securitySession, String room, String person, String actor) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("500", String.format("不存在聊天室。%s %s", securitySession.principal(), room));
        }
        if (chatroomService.existsMember(chatroom.getCreator(), room, person)) {
            throw new CircuitException("500", "成员已在聊天室。");
        }
        RoomMember member = new RoomMember();
        member.setActor(actor);
        member.setAtime(System.currentTimeMillis());
        member.setNickName(null);
        member.setPerson(person);
        member.setRoom(room);
        chatroomService.addMember(chatroom.getCreator(), member);
    }

    @Override
    public void removeMember(ISecuritySession securitySession, String room, String person) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            return;
        }
        chatroomService.removeMember(chatroom.getCreator(), room, person);
    }

    @Override
    public List<RoomMember> pageRoomMember(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            return new ArrayList<>();
        }
        return chatroomService.pageRoomMember(chatroom.getCreator(), room, limit, offset);
    }

    @Override
    public List<RoomMember> pageRoomMembersOfPerson(ISecuritySession securitySession, String room, String creator, int limit, long offset) throws CircuitException {
        return chatroomService.pageRoomMember(creator, room, limit, offset);
    }

    @Override
    public List<RoomMember> getActorRoomMembers(ISecuritySession securitySession, String room, String actor) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            return new ArrayList<>();
        }
        return chatroomService.getActorRoomMembers(chatroom.getCreator(), room, actor);
    }

    @Override
    public RoomMember getRoomMember(ISecuritySession securitySession, String room, String creator) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(creator, room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        return chatroomService.getRoomMember(chatroom, securitySession.principal());
    }

    @Override
    public RoomMember getHisRoomMember(ISecuritySession securitySession, String room, String creator, String person) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(creator, room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        return chatroomService.getRoomMember(chatroom, person);
    }

    @Override
    public void updateNickName(ISecuritySession securitySession, String room, String creator, String nickName) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(creator, room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        chatroomService.updateNickName(chatroom.getCreator(), room, securitySession.principal(), nickName);
    }

    @Override
    public void updateBackground(ISecuritySession securitySession, String room, String background) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        chatroomService.updateBackground(chatroom.getCreator(), room,  background);
    }

    @Override
    public void setShowNick(ISecuritySession securitySession, String room, String creator, boolean isShowNick) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(creator, room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        chatroomService.setShowNick(chatroom, securitySession.principal(), isShowNick);
    }

    @Override
    public void updateLeading(ISecuritySession securitySession, String room, String leading) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        chatroomService.updateLeading(chatroom.getCreator(), room, leading);
    }

    @Override
    public void updateTitle(ISecuritySession securitySession, String room, String title) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        chatroomService.updateTitle(chatroom.getCreator(), room, title);
    }

    @Override
    public void publishNotice(ISecuritySession securitySession, String room, String notice) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }

        RoomMember member = chatroomService.getMember(chatroom.getCreator(), room, securitySession.principal());
        if (member == null) {
            throw new CircuitException("404", "不是聊天室成员");
        }
        if ("user".equals(member.getActor())) {
            throw new CircuitException("404", "无发布权限");
        }
        RoomNotice roomNotice = new RoomNotice();
        roomNotice.setCreator(securitySession.principal());
        roomNotice.setCtime(System.currentTimeMillis());
        roomNotice.setNotice(notice);
        roomNotice.setRoom(room);
        chatroomService.addNotice(chatroom.getCreator(), roomNotice);
    }

    @Override
    public RoomNotice getNewestNotice(ISecuritySession securitySession, String room) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }
        return chatroomService.getNewestNotice(chatroom.getCreator(), room);
    }

    @Override
    public List<RoomNotice> pageNotice(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        Chatroom chatroom = chatroomService.getRoom(securitySession.principal(), room);
        if (chatroom == null) {
            return new ArrayList<>();
        }
        return chatroomService.pageNotice(chatroom.getCreator(), room, limit, offset);
    }
}
