package cj.netos.link.entities;

public class ChannelInputPerson {
    String person;
    String channel;
    String onchannel;
    long atime;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getOnchannel() {
        return onchannel;
    }

    public void setOnchannel(String onchannel) {
        this.onchannel = onchannel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getAtime() {
        return atime;
    }

    public void setAtime(long atime) {
        this.atime = atime;
    }
}