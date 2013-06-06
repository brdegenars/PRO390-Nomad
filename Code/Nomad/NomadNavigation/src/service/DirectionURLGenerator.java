package service;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/28/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectionURLGenerator {

    private static final boolean SENSOR_OFF = false;
    private static final String TRANSIT_MODE = "driving";
    private static final String AVOID_HIGHWAY = "highway";
    private static final String AVOID_TOLLS = "tolls";
    private static final String UNIT_METRIC = "metric";
    private static final String UNIT_IMPERIAL = "imperial";
    private static final String URL_ENCODING = "UTF-8";

    public static String generateURL(String origin, String destination, LatLng waypoint){

        String url = "http://maps.googleapis.com/maps/api/directions/json?";

        url = extractParameter(url, "origin", new String[]{origin}, false);
        url = extractParameter(url, "destination", new String[]{destination}, false);
        url = extractParameter(url, "sensor", new String[]{String.valueOf(SENSOR_OFF)}, false);
        url = extractParameter(url, "mode", new String[]{TRANSIT_MODE}, false);
        if (waypoint != null) url = extractParameter(url, "waypoints", new String[]{waypoint.latitude + "," + waypoint.longitude}, false);
        //url = extractParameter(url, "avoid", null, false);
        url = extractParameter(url, "units", new String[]{UNIT_IMPERIAL}, true);
        //url.replace(".", "%2E");

        return url;
    }

    private static String extractParameter(String url, String parameter, String[] params, boolean lastParam){

        if(parameter.equals("waypoints") && params.length > 1){

            url = url.concat(parameter + "=via:" + params[0]);
            for(int i = 1; i < params.length; i++) url = url.concat("%7Cvia:" + params[i]);

        } else
            url = url.concat(parameter + "=" + params[0]);

        if(!lastParam) url = url.concat("&");

        return url;
    }
}
