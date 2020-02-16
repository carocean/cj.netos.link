package cj.netos.link.entities;

public class Channel {
    String code;
    String name;
    String leading;
    String creator;
    String loopType;
    String outPersonSelector;//only_select, all_except,
    String outGeoSelector;//true,false;
    long ctime;

    public String getLoopType() {
        return loopType;
    }

    public void setLoopType(String loopType) {
        this.loopType = loopType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
