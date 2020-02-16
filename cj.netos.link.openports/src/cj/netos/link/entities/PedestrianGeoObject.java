package cj.netos.link.entities;

/**
 * 行人地理对<br>
 *     可移动的地理对象每种独立一张表
 */
public class PedestrianGeoObject {
    String person;
    Location location;
    long utime;
    String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }
}
