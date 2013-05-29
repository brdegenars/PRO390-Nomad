package service;

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

    public static String generateURL(String origin, String destination){

        String url = "http://maps.googleapis.com/maps/api/directions/json?";

        url = extractParameter(url, "origin", new String[]{origin}, false);
        url = extractParameter(url, "destination", new String[]{destination}, false);
        url = extractParameter(url, "sensor", new String[]{String.valueOf(SENSOR_OFF)}, false);
        url = extractParameter(url, "mode", new String[]{TRANSIT_MODE}, false);
        //url = extractParameter(url, "waypoints", params.get(4), false);
        //url = extractParameter(url, "avoid", null, false);
        url = extractParameter(url, "units", new String[]{UNIT_IMPERIAL}, true);

        return url;
    }

    private static String extractParameter(String url, String parameter, String[] params, boolean lastParam){

        if(parameter.equals("way-points") && params.length > 1){

            url = url.concat(parameter + "=" + params[0] + "|");

            for(int i = 1; i < params.length - 1; i++)
                url = url.concat(params[i] + "|");

            url = url.concat(params[params.length-1] + "&");
        } else
            url = url.concat(parameter + "=" + params[0]);

        if(!lastParam) url = url.concat("&");

        return url;
    }
}
