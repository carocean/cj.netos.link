package cj.netos.link.entities;

public class Channel {
    String channel;
    String title;
    String leading;
    String creator;
    String upstreamPerson;
    String inPersonSelector;
    String outPersonSelector;//only_select, all_except,
    String outGeoSelector;//true,false;
    long ctime;

    public String getInPersonSelector() {
        return inPersonSelector;
    }

    public void setInPersonSelector(String inPersonSelector) {
        this.inPersonSelector = inPersonSelector;
    }

    public String getUpstreamPerson() {
        return upstreamPerson;
    }

    public void setUpstreamPerson(String upstreamPerson) {
        this.upstreamPerson = upstreamPerson;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeading() {
        return leading;
    }

    public void setLeading(String leading) {
        this.leading = leading;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getOutPersonSelector() {
        return outPersonSelector;
    }

    public void setOutPersonSelector(String outPersonSelector) {
        this.outPersonSelector = outPersonSelector;
    }

    public String getOutGeoSelector() {
        return outGeoSelector;
    }

    public void setOutGeoSelector(String outGeoSelector) {
        this.outGeoSelector = outGeoSelector;
    }
}
