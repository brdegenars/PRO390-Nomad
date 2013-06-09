package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import service.DirectionURLGenerator;
import service.ServiceRequest;
import service.TrafficURLGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/21/13
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapHandler extends Activity {

    private final String TEST_JSON_URL = "http://dev.virtualearth.net/REST/V1/Traffic/Incidents/37,-105,45,-94/?t=9,2&s=2,3&key=" + TrafficURLGenerator.API_KEY_VALUE;

    private GoogleMap navigationMap;
    private PolylineOptions routeLineOptions;
    private List<LatLng> currentRoute = new ArrayList<LatLng>(){
        @Override
        public boolean contains(Object loc){
            LatLng location = (LatLng)loc;
            for(LatLng point : this){
                if (Math.abs(point.latitude - location.latitude) < 0.01 && (point.longitude - location.longitude) < 0.01) return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);

        LocationManager locationManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, currentLocationListener);
    }

    @Override
    protected void onResume(){
        super.onResume();

        navigationMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapDisplay)).getMap();

        navigationMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        navigationMap.setMyLocationEnabled(true);
        //navigationMap.setTrafficEnabled(true);

        SharedPreferences localData = getSharedPreferences(NavigationPrompt.NAV_PREFERENCES, Activity.MODE_PRIVATE);
        //SharedPreferences.Editor localDataEditor = localData.edit();

        JSONObject directionJSONObject, trafficJSONObject;

        String originLocation = localData.getString(NavigationPrompt.NAV_ORIGIN, null);
        String destinationLocation = localData.getString(NavigationPrompt.NAV_DESTINATION, null);
        LatLng waypoint = null;
        List<JSONObject> incidents;

//        do {
            try {
                directionJSONObject = getDirectionJsonObject(originLocation, destinationLocation, waypoint);

                String[] cardinalBounds = drawDirectionPath(directionJSONObject);

                //trafficJSONObject = new ServiceRequest().execute(TrafficURLGenerator.generateURL(cardinalBounds, new String[]{"3", "4"}, new String[]{"1", "2", "6", "7", "8", "10", "11"})).get();
                trafficJSONObject = testTrafficData();

                incidents = checkForIncidents(trafficJSONObject);
                if (incidents != null) {
                    int[] hinderingIncident = findIncidentAlongRoute(incidents);
                    if (hinderingIncident[0] != currentRoute.size()){
                        waypoint = selectDivertingWaypoint(hinderingIncident);
                        directionJSONObject = getDirectionJsonObject(originLocation, destinationLocation, waypoint);

                        cardinalBounds = drawDirectionPath(directionJSONObject);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("ERROR : Current thread was interrupted");
            } catch (ExecutionException e) {
                System.out.println("ERROR: AsyncTask was unable to execute successfully.");
            } catch (JSONException e) {
                System.out.println("ERROR: Invalid traffic JSON object");
            }
//        } while (!incidents.isEmpty());
        navigationMap.addPolyline(routeLineOptions.color(Color.BLUE));
    }

    private JSONObject getDirectionJsonObject(String originLocation, String destinationLocation, LatLng waypoint) throws InterruptedException, ExecutionException {
        JSONObject directionJSONObject;
        directionJSONObject = new ServiceRequest().
                execute(DirectionURLGenerator.generateURL(originLocation, destinationLocation, waypoint)).get();
        return directionJSONObject;
    }

    private JSONObject testTrafficData() throws JSONException {

        String testJSON = "{\n" +
                "   \"authenticationResultCode\":\"ValidCredentials\",\n" +
                "   \"brandLogoUri\":\"http:\\/\\/dev.virtualearth.net\\/Branding\\/logo_powered_by.png\",\n" +
                "   \"copyright\":\"Copyright © 2011 Microsoft and its suppliers. All rights reserved. This API cannot be accessed and the content and any results may not be used, reproduced or transmitted in any manner without express written permission from Microsoft Corporation.\",\n" +
                "   \"resourceSets\":[\n" +
                "      {\n" +
                "         \"estimatedTotal\":131,\n" +
                "         \"resources\":[\n" +
                "            {\n" +
                "               \"__type\":\"TrafficIncident:http:\\/\\/schemas.microsoft.com\\/search\\/local\\/ws\\/rest\\/v1\",\n" +
                "               \"point\":{\n" +
                "                  \"type\":\"Point\",\n" +
                "                  \"coordinates\":[\n" +
                "                     40.63066,\n" +
                "                     -111.90382\n" +
                "                  ]\n" +
                "               },\n" +
                "               \"congestion\":\"\",\n" +
                "               \"description\":\"\",\n" +
                "               \"detour\":\"\",\n" +
                "               \"end\":\"\\/Date(1310396400000)\\/\",\n" +
                "               \"incidentId\":210546697,\n" +
                "               \"lane\":\"\",\n" +
                "               \"lastModified\":\"\\/Date(1309391096593)\\/\",\n" +
                "               \"roadClosed\":true,\n" +
                "               \"severity\":3,\n" +
                "               \"start\":\"\\/Date(1307365200000)\\/\",\n" +
                "               \"type\":1,\n" +
                "               \"verified\":true\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"statusCode\":200,\n" +
                "   \"statusDescription\":\"OK\",\n" +
                "   \"traceId\":\"38491198bf6a42f5b7e60c18aa08ec02\"\n" +
                "}";
        return new JSONObject(testJSON);
    }

    private String[] drawDirectionPath(JSONObject directionJSONObject){

        if (directionJSONObject == null) return null;
        if (!directionJSONObject.opt("status").equals("OK")) return null;

        int FIRST_ROUTE_POSITION = 0;
        //int FIRST_LEG_POSITION = 0;

        JSONArray routes = directionJSONObject.optJSONArray("routes");
        JSONObject firstRoute = routes.optJSONObject(FIRST_ROUTE_POSITION);

        JSONObject smoothPolyline = firstRoute.optJSONObject("overview_polyline");
        String polylinePoints = (String)smoothPolyline.opt("points");

        System.out.println("Polyline points : \n" + polylinePoints);

        //JSONArray legs = firstRoute.optJSONArray("legs");
        //JSONObject firstLeg = legs.optJSONObject(FIRST_LEG_POSITION);

        routeLineOptions = new PolylineOptions();

        List<LatLng> polylineLatLng = decodePoly(polylinePoints);

        currentRoute.clear();

        double maxDifLat = (polylineLatLng.get(0).latitude - polylineLatLng.get(1).latitude);
        double maxDifLng = (polylineLatLng.get(0).longitude - polylineLatLng.get(1).longitude);
        currentRoute.add(polylineLatLng.get(0));

        for(int i = 1; i < polylineLatLng.size(); i++){
            currentRoute.add(polylineLatLng.get(i));
            System.out.println("Point along path: " + polylineLatLng.get(i).latitude + ", " + polylineLatLng.get(i).longitude);
            double currentLatDif = (polylineLatLng.get(i).latitude - polylineLatLng.get(i - 1).latitude);
            double currentLngDif = (polylineLatLng.get(i).longitude - polylineLatLng.get(i - 1).longitude);
            if (currentLatDif > maxDifLat) maxDifLat = currentLatDif;
            if (currentLngDif > maxDifLng) maxDifLng = currentLngDif;
        }

        System.out.println("MAX LAT DIFF BETWEEN POINTS: " + maxDifLat);
        System.out.println("MAX LNG DIFF BETWEEN POINTS: " + maxDifLng);

        LatLng firstPolylineLatLng = polylineLatLng.get(0);

        double NLat = firstPolylineLatLng.latitude,
               SLat = firstPolylineLatLng.latitude,
               ELng = firstPolylineLatLng.longitude,
               WLng = firstPolylineLatLng.longitude;

        for (LatLng polylineLatLngPoint : polylineLatLng){

            double currentLng = polylineLatLngPoint.longitude;
            double currentLat = polylineLatLngPoint.latitude;

            if (currentLat < SLat) SLat = currentLat;
            else if (currentLat > NLat) NLat = currentLat;

            if (currentLng < WLng) WLng = currentLng;
            else if (currentLng > ELng) ELng = currentLng;
            if (!routeLineOptions.getPoints().contains(polylineLatLngPoint))
                routeLineOptions.add(polylineLatLngPoint);
            else System.out.println("DUPLICATE POINT: " + polylineLatLngPoint.latitude + ", " + polylineLatLngPoint.longitude);
        }
        navigationMap.addPolyline(routeLineOptions.color(Color.RED));
        //animateToHere(originLatLng);

        return new String[] {String.valueOf(SLat), String.valueOf(WLng), String.valueOf(NLat), String.valueOf(ELng)};
    }

    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @SuppressWarnings("unchecked")
    private List<JSONObject> checkForIncidents(JSONObject trafficJSONObjcet) throws JSONException {

        int RESOURCE_SET_POSITION = 0;

        List<JSONObject> incidentList = new ArrayList<JSONObject>();

        JSONArray resourceSets = trafficJSONObjcet.optJSONArray("resourceSets");
        JSONObject resourceSet = resourceSets.optJSONObject(RESOURCE_SET_POSITION);
        JSONArray incidents = resourceSet.optJSONArray("resources");
        JSONObject incident = incidents.optJSONObject(0);
        incidentList.add(incident);

        return incidentList;
    }

    private int[] findIncidentAlongRoute(List<JSONObject> incidents) throws JSONException {

        int closestStartIndex = currentRoute.size();
        int closestEndIndex = -1;

        for (JSONObject incident : incidents) {

            JSONObject startPoint = incident.optJSONObject("point");
            JSONObject endPoint = incident.optJSONObject("toPoint");

            JSONArray startPointCoordinates = startPoint.optJSONArray("coordinates");
            JSONArray endPointCoordinates = null;
            if (endPoint != null ) endPointCoordinates = endPoint.optJSONArray("coordinates");

            LatLng incidentStartPoint = extractCoordinates(startPointCoordinates);
            LatLng incidentEndPoint = null;
            if (endPointCoordinates != null) incidentEndPoint = extractCoordinates(endPointCoordinates);

            if (currentRoute.contains(incidentStartPoint))
                for (int j = 0; j < currentRoute.size(); j++)
                    if (Math.abs(currentRoute.get(j).latitude - incidentStartPoint.latitude) < 0.01 && (currentRoute.get(j).longitude - incidentStartPoint.longitude) < 0.01)
                        if (j < closestStartIndex)
                            closestStartIndex = j;

            if (endPoint != null && incidentEndPoint != null)
                for (int j = 0; j < currentRoute.size(); j++)
                    if (currentRoute.get(j).longitude == incidentEndPoint.longitude && currentRoute.get(j).latitude == incidentEndPoint.latitude)
                        if (j > closestEndIndex && j > closestStartIndex) closestStartIndex = j;

        }
        return new int[] {closestStartIndex, closestEndIndex};
    }

    private LatLng extractCoordinates(JSONArray coordinates) throws JSONException {
        int LAT_POSITION = 0, LNG_POSITION = 1;

        double lat, lng;
        lat = coordinates.getDouble(LAT_POSITION);
        lng = coordinates.getDouble(LNG_POSITION);

        return new LatLng(lat, lng);
    }

    private LatLng selectDivertingWaypoint(int[] incidentPointIndices){

        double FIFTEEN_PERCENT = .15;

        LatLng destination = currentRoute.get(currentRoute.size() - 1);
        LatLng incidentStart = currentRoute.get(incidentPointIndices[0]);
        LatLng incidentEnd;

        if (incidentPointIndices[1] != -1) incidentEnd = currentRoute.get(incidentPointIndices[1]);

        double newLat = 0.0, newLng = 0.0;
        String direction = "";

        if (destination.latitude >= incidentStart.latitude) direction = direction.concat("N");
        else if (destination.latitude < incidentStart.latitude) direction = direction.concat("S");

        if(destination.longitude >= incidentStart.longitude) direction = direction.concat("E");
        else if (destination.longitude < incidentStart.longitude) direction = direction.concat("W");

        double lngDifference = Math.abs(destination.longitude - incidentStart.longitude);
        double latDifference = Math.abs(destination.latitude - incidentStart.latitude);

        if (direction.equals("NW")){
            newLat = (incidentStart.latitude - (latDifference * FIFTEEN_PERCENT));
            newLng = (incidentStart.longitude - (lngDifference * FIFTEEN_PERCENT));
        } else if (direction.equals("NE")){
            newLat = (incidentStart.latitude - (latDifference * FIFTEEN_PERCENT));
            newLng = (incidentStart.longitude + (lngDifference * FIFTEEN_PERCENT));
        } else if (direction.equals("SW")){
            newLat = (incidentStart.latitude + (latDifference * FIFTEEN_PERCENT));
            newLng = (incidentStart.longitude + (lngDifference * FIFTEEN_PERCENT));
        } else if (direction.equals("SE")){
            newLat = (incidentStart.latitude + (latDifference * FIFTEEN_PERCENT));
            newLng = (incidentStart.longitude - (lngDifference * FIFTEEN_PERCENT));
        }
        return new LatLng(newLat, newLng);
    }

    private void animateToHere(LatLng location){
         navigationMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    private LocationListener currentLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //animateToHere(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
}
