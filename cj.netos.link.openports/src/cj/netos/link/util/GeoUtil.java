package cj.netos.link.util;

import cj.netos.link.entities.geo.LatLng;

public class GeoUtil {

    /*
     * 计算两点之间距离
     * @param start
     * @param end
     * @return 米
     */
   public static double getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude();
        double lat2 = (Math.PI / 180) * end.latitude();

        double lon1 = (Math.PI / 180) * start.longitude();
        double lon2 = (Math.PI / 180) * end.longitude();

//      double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);
//      double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);
//      double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);
//      double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);

        //地球半径
        double R = 6371;

        //两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) *
                R;

        return d * 1000;
    }
}
