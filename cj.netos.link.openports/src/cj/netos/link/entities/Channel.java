package cj.netos.link.entities;

public class Channel {
    String channel;
    String title;
    String leading;
    String creator;
    String upstreamPerson;
    String sourceCreator;//源管道的创建者
    String inPersonSelector;
    String outPersonSelector;//only_select, all_except,
    String outGeoSelector;//true,false;
    int delFlag;//0为可用；1为标记删除
    int isChanged;//管道资料是否有更新；0为无；1为标题更新；2为管道图标更新；供追链更新管道资料，抽数后设为0
    long ctime;

    public int getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(int isChanged) {
        this.isChanged = isChanged;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getSourceCreator() {
        return sourceCreator;
    }

    public void setSourceCreator(String sourceCreator) {
        this.sourceCreator = sourceCreator;
    }

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
