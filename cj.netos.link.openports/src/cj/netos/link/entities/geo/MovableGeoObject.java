package cj.netos.link.entities.geo;

/**
 * 可移动的地理对象<br>
 *     <pre>
 *         为了性能，将位置更新较少的固定地物单独存表，而将数据量较大的如行人、网约车单独存表，其它的可移动对象存入此表
 *     </pre>
 */
public class MovableGeoObject {
    Object body;//可移动的物体信息
    String master;//可移动物体的主人，如狗的主人
    String category;//地理对象所属类别，为两级分类，如：/行/公交/；固定地物的分类参考高德地图附近中的分类，这些分类均是固定地物
    Location location;
    long utime;
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
