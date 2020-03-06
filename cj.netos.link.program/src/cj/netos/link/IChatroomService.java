package cj.netos.link;

import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;

import java.util.List;

public interface IChatroomService {
    Chatroom getRoom(String principal,String room);

    void addRoom(String principal,Chatroom chatroom);

    void removeRoom(String principal,String room);

    List<Chatroom> pageRoom(String principal,int limit ,long offset);

    void addMember(String principal,RoomMember member);

    boolean existsMember(String principal,String room, String person);

    void removeMember(String principal,String room, String person);

    List<RoomMember> pageAnyRoomMember(String principal,String room, int limit, long offset);

    List<RoomMember> getActorRoomMembers(String principal,String room, String actor);

    void updateNickName(String principal,String room, String person, String nickName);

    void addNotice(String principal,RoomNotice roomNotice);

    RoomNotice getNewestNotice(String principal,String room);

    List<RoomNotice> pageNotice(String principal,String room, int limit, long offset);

    RoomMember getMember(String principal,String room,String person);

    void updateLeading(String principal, String room, String leading);

    void updateTitle(String principal, String room, String title);

}

