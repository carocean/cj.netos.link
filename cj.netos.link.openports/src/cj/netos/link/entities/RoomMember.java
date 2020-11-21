package cj.netos.link.entities;

public class RoomMember {
    String room;
    String person;
    String actor;//创建者(creator)管理员(admin)，客服(servicer)，普通成员(user)
    String nickName;
    String inviter;//邀请人
    int flag;//0为可用；1为已被删除
    boolean isShowNick;
    long atime;//加入时间

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isShowNick() {
        return isShowNick;
    }

    public void setShowNick(boolean showNick) {
        isShowNick = showNick;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getAtime() {
        return atime;
    }

    public void setAtime(long atime) {
        this.atime = atime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
