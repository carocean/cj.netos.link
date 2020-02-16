package cj.netos.link;

import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;

import java.util.List;

public interface IChatroomService {
    Chatroom getRoom(String room);

    void addRoom(Chatroom chatroom);

    void removeRoom(String room);

    List<Chatroom> pageRoom(String person,int limit ,long offset);

    void addMember(RoomMember member);

    boolean existsMember(String room, String person);

    void removeMember(String room, String person);

    List<RoomMember> pageAnyRoomMember(String room, int limit, long offset);

    List<RoomMember> getActorRoomMembers(String room, String actor);

    void updateNickName(String room, String person, String nickName);

    void addNotice(RoomNotice roomNotice);

    RoomNotice getNewestNotice(String room);

    List<RoomNotice> pageNotice(String room, int limit, long offset);

    RoomMember getMember(String room, String principal);

}

