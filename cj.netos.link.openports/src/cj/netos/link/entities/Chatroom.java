package cj.netos.link.entities;

public class Chatroom {
    String room;
    String title;
    String creator;
    String leading;
    String microsite;
    String background;
    boolean isForegroundWhite;
    long ctime;
    public String getMicrosite() {
        return microsite;
    }

    public void setMicrosite(String microsite) {
        this.microsite = microsite;
    }

    public boolean isForegroundWhite() {
        return isForegroundWhite;
    }

    public void setForegroundWhite(boolean foregroundWhite) {
        isForegroundWhite = foregroundWhite;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLeading() {
        return leading;
    }

    public void setLeading(String leading) {
        this.leading = leading;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
