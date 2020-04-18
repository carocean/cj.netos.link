package cj.netos.link.ports;

import cj.netos.link.entities.Chatroom;
import cj.netos.link.entities.RoomMember;
import cj.netos.link.entities.RoomNotice;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.ISecuritySession;
import cj.studio.openport.PKeyInRequest;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

@CjOpenports(usage = "聊天室个人自助服务")
public interface IChatroomLinkPorts extends IOpenportService {

    @CjOpenport(usage = "创建聊天室")
    void createRoom(ISecuritySession securitySession,
                    @CjOpenportParameter(usage = "聊天室编号", name = "id") String id,
                    @CjOpenportParameter(usage = "聊天室显示名", name = "title") String title,
                    @CjOpenportParameter(usage = "聊天室头像", name = "leading") String leading,
                    @CjOpenportParameter(usage = "聊天室关联站点", name = "microsite") String microsite) throws CircuitException;


    @CjOpenport(usage = "移除聊天室")
    void removeRoom(ISecuritySession securitySession,
                    @CjOpenportParameter(usage = "聊天室标识", name = "room") String room) throws CircuitException;

    @CjOpenport(usage = "获取聊天室信息")
    Chatroom getRoom(ISecuritySession securitySession,
                     @CjOpenportParameter(usage = "聊天室标识", name = "room") String room) throws CircuitException;

    @CjOpenport(usage = "获取指定用户创建的聊天室信息")
    Chatroom getRoomOfPerson(ISecuritySession securitySession,
                             @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                             @CjOpenportParameter(usage = "公众号", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "分页聊天室")
    List<Chatroom> pageRoom(ISecuritySession securitySession,
                            @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                            @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                            @CjOpenportParameter(usage = "当前偏移", name = "offset") long offset) throws CircuitException;


    @CjOpenport(usage = "为聊天室添加成员")
    void addMember(ISecuritySession securitySession,
                   @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                   @CjOpenportParameter(usage = "成员名", name = "person") String person,
                   @CjOpenportParameter(usage = "成员扮演的角色，有：客服(servicer)，普通成员(user)", name = "actor") String actor) throws CircuitException;


    @CjOpenport(usage = "为聊天室移除成员")
    void removeMember(ISecuritySession securitySession,
                      @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                      @CjOpenportParameter(usage = "成员名", name = "person") String person) throws CircuitException;


    @CjOpenport(usage = "分页查询任意角色的成员")
    List<RoomMember> pageRoomMember(ISecuritySession securitySession,
                                    @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                                    @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                                    @CjOpenportParameter(usage = "当前偏移", name = "offset") long offset) throws CircuitException;

    @CjOpenport(usage = "获取访问者在聊天室的成员属性")
    RoomMember getRoomMember(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
            @CjOpenportParameter(usage = "聊天室创建者", name = "creator") String creator
    ) throws CircuitException;

    @CjOpenport(usage = "获取指定用户在聊天室的成员属性")
    RoomMember getHisRoomMember(
            ISecuritySession securitySession,
            @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
            @CjOpenportParameter(usage = "聊天室创建者", name = "creator") String creator,
            @CjOpenportParameter(usage = "成员", name = "person") String person
    ) throws CircuitException;

    @CjOpenport(usage = "分页查询指定角色的成员")
    List<RoomMember> getActorRoomMembers(ISecuritySession securitySession,
                                         @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                                         @CjOpenportParameter(usage = "成员扮演的角色，有：创建者(creator)管理员(admin)，客服(servicer)，普通成员(user)", name = "actor") String actor) throws CircuitException;

    @CjOpenport(usage = "分页查询成员")
    List<RoomMember> pageRoomMembersOfPerson(ISecuritySession securitySession,
                                             @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                                             @CjOpenportParameter(usage = "聊天室创建者", name = "creator") String creator,
                                             @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                                             @CjOpenportParameter(usage = "当前偏移", name = "offset") long offset
    ) throws CircuitException;


    @CjOpenport(usage = "更新我在聊天室中的昵称")
    void updateNickName(ISecuritySession securitySession,
                        @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                        @CjOpenportParameter(usage = "聊天室创建者", name = "creator") String creator,
                        @CjOpenportParameter(usage = "成员在聊天室中的昵称", name = "nickName") String nickName) throws CircuitException;


    @CjOpenport(usage = "设置是否显示为昵称")
    void setShowNick(ISecuritySession securitySession,
                     @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                     @CjOpenportParameter(usage = "聊天室创建者", name = "creator") String creator,
                     @CjOpenportParameter(usage = "是否显示在群中的昵称", name = "isShowNick") boolean isShowNick) throws CircuitException;


    @CjOpenport(usage = "更新聊天室图标")
    void updateLeading(ISecuritySession securitySession,
                       @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                       @CjOpenportParameter(usage = "聊天室图标", name = "leading") String leading) throws CircuitException;

    @CjOpenport(usage = "更新聊天室显示名")
    void updateTitle(ISecuritySession securitySession,
                     @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                     @CjOpenportParameter(usage = "聊天室显示名", name = "title") String title) throws CircuitException;

    @CjOpenport(usage = "更新聊天室(必须是访问者创建的）背景")
    void updateBackground(ISecuritySession securitySession,
                     @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                     @CjOpenportParameter(usage = "背景url", name = "background") String background) throws CircuitException;

    @CjOpenport(usage = "发布公告", command = "post")
    void publishNotice(ISecuritySession securitySession,
                       @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                       @CjOpenportParameter(usage = "公告内容", name = "notice", in = PKeyInRequest.content) String notice) throws CircuitException;


    @CjOpenport(usage = "获取最新公告")
    RoomNotice getNewestNotice(ISecuritySession securitySession,
                               @CjOpenportParameter(usage = "聊天室标识", name = "room") String room) throws CircuitException;


    @CjOpenport(usage = "按页查询历史公告")
    List<RoomNotice> pageNotice(ISecuritySession securitySession,
                                @CjOpenportParameter(usage = "聊天室标识", name = "room") String room,
                                @CjOpenportParameter(usage = "页大小", name = "limit") int limit,
                                @CjOpenportParameter(usage = "当前偏移", name = "offset") long offset) throws CircuitException;
}
