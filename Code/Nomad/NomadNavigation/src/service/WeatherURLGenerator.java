package service;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 6/10/13
 * Time: 5:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeatherURLGenerator {

    private final static String BASE_URL = " http://A7659599597.api.wxbug.net/getAlertsRSS.aspx?ACode=A7659599597&";
    private final static int IMPERIAL_UNITS = 0;

    public static String generateURL(LatLng location){

        String lat = String.valueOf(location.latitude);
        String lng = String.valueOf(location.longitude);

        String[] latitude = lat.split(".");
        latitude[1] = latitude[1].substring(0,1);

        String[] longitude = lng.split(".");
        longitude[1] = longitude[1].substring(0,1);

        lat = latitude[0].concat(latitude[1]);
        lng = longitude[0].concat(latitude[1]);

        return BASE_URL.concat("lat=" + lat + "&long=" + lng + "&unittype=" + IMPERIAL_UNITS);
    }
}
