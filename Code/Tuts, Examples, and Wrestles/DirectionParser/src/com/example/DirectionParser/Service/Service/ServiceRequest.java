package com.example.DirectionParser.Service.Service;

import com.example.DirectionParser.Service.RequestFactory.RequestFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/11/13
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceRequest {

    private final String TRANSIT_MODE = "driving";

    private RequestFactory requestFactory = new RequestFactory();

    public void request(String origin, String destination, boolean sensor, String[] waypoints, String avoid, String units){

        ArrayList<String[]> paramValues = setRequestedParameters(origin, destination, sensor, waypoints, avoid, units);
        requestFactory.makeRequest(paramValues);
    }

    private ArrayList<String[]> setRequestedParameters(String origin, String destination, boolean sensor, String[] waypoints, String avoid, String units) {

        ArrayList<String[]> paramValues = new ArrayList<String[]>();

        paramValues.add(new String[]{origin});
        paramValues.add(new String[] {destination});
        paramValues.add(new String[] {String.valueOf(sensor)});
        paramValues.add(new String[]{TRANSIT_MODE});
        paramValues.add(waypoints);
        paramValues.add(new String[] {avoid});
        paramValues.add(new String[] {units});

        return paramValues;
    }
}
