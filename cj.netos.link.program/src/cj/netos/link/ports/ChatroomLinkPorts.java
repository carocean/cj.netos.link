package cj.netos.link.ports;

import cj.netos.link.IChatroomService;
import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.ISecuritySession;

import java.util.List;

@CjService(name = "/chatroom/self.service")
public class ChatroomLinkPorts implements IChatroomLinkPorts {
    @CjServiceRef
    IChatroomService chatroomService;

    @Override
    public void createRoom(ISecuritySession securitySession, String code, String title, String leading, String microsite) throws CircuitException {
        String room = String.format("%s/%s", securitySession.principal(), code);
        if (chatroomService.getRoom(securitySession.principal(), room) != null) {
            throw new CircuitException("500", "已存在聊天室");
        }
        Chatroom chatroom = new Chatroom();
        chatroom.setRoom(String.format("%s/%s", securitySession.principal(), code));
        chatroom.setCreator(securitySession.principal());
        chatroom.setCtime(System.currentTimeMillis());
        chatroom.setLeading(leading);
        chatroom.setMicrosite(microsite);
        chatroom.setTitle(title);
        chatroomService.addRoom(securitySession.principal(), chatroom);
    }

    @Override
    public void removeRoom(ISecuritySession securitySession, String room) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能移除其他人的聊天室");
        }
        chatroomService.removeRoom(securitySession.principal(), room);
    }

    @Override
    public Chatroom getRoom(ISecuritySession securitySession, String room) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        return chatroomService.getRoom(owner, room);
    }

    @Override
    public List<Chatroom> pageRoom(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能列出他人的聊天室");
        }
        return chatroomService.pageRoom(securitySession.principal(), limit, offset);
    }

    @Override
    public void addMember(ISecuritySession securitySession, String room, String person, String actor) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        Chatroom chatroom = chatroomService.getRoom(owner, room);
        if (chatroom == null) {
            throw new CircuitException("500", "不存在聊天室");
        }
        if (chatroomService.existsMember(owner, room, person)) {
            throw new CircuitException("500", "成员已在聊天室");
        }
        RoomMember member = new RoomMember();
        member.setActor(actor);
        member.setOwner(chatroom.getCreator());
        member.setAtime(System.currentTimeMillis());
        member.setNickName(null);
        member.setPerson(person);
        member.setRoom(room);
        chatroomService.addMember(securitySession.principal(), member);
    }

    @Override
    public void removeMember(ISecuritySession securitySession, String room, String person) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能移除他人的聊天室成员");
        }
        chatroomService.removeMember(securitySession.principal(), room, person);
    }

    @Override
    public List<RoomMember> pageAnyRoomMember(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        return chatroomService.pageAnyRoomMember(owner, room, limit, offset);
    }

    @Override
    public List<RoomMember> getActorRoomMembers(ISecuritySession securitySession, String room, String actor) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        return chatroomService.getActorRoomMembers(owner, room, actor);
    }

    @Override
    public void updateNickName(ISecuritySession securitySession, String room, String person, String nickName) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能修改他人在本聊天室的昵称");
        }
        chatroomService.updateNickName(securitySession.principal(), room, person, nickName);
    }

    @Override
    public void updateLeading(ISecuritySession securitySession, String room, String leading) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能修改他人的聊天室");
        }
        chatroomService.updateLeading(securitySession.principal(), room, leading);
    }

    @Override
    public void updateTitle(ISecuritySession securitySession, String room, String title) throws CircuitException {
        if (!room.startsWith(securitySession.principal())) {
            throw new CircuitException("500", "不能修改他人的聊天室");
        }
        chatroomService.updateTitle(securitySession.principal(), room, title);
    }

    @Override
    public void publishNotice(ISecuritySession securitySession, String room, String notice) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        Chatroom chatroom = chatroomService.getRoom(owner, room);
        if (chatroom == null) {
            throw new CircuitException("404", "聊天室不存在");
        }

        RoomMember member = chatroomService.getMember(owner, room, securitySession.principal());
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
        chatroomService.addNotice(owner, roomNotice);
    }

    @Override
    public RoomNotice getNewestNotice(ISecuritySession securitySession, String room) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        return chatroomService.getNewestNotice(owner, room);
    }

    @Override
    public List<RoomNotice> pageNotice(ISecuritySession securitySession, String room, int limit, long offset) throws CircuitException {
        String owner = room.substring(0, room.indexOf("/"));
        return chatroomService.pageNotice(owner, room, limit, offset);
    }
}
