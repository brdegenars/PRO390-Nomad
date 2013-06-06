package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import service.ServiceRequest;
import service.DirectionURLGenerator;
import service.TrafficURLGenerator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    private final int RESOURCE_SET_POSITION = 2;

    private final String TEST_JSON_URL = "http://dev.virtualearth.net/REST/V1/Traffic/Incidents/37,-105,45,-94/?t=9,2&s=2,3&key=" + TrafficURLGenerator.API_KEY_VALUE;

    private GoogleMap navigationMap;
    private List<LatLng> currentRoute = new ArrayList<LatLng>();

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
        navigationMap.setTrafficEnabled(true);

        SharedPreferences localData = getSharedPreferences(NavigationPrompt.NAV_PREFERENCES, Activity.MODE_PRIVATE);
        SharedPreferences.Editor localDataEditor = localData.edit();

        JSONObject directionJSONObject, trafficJSONObject = null;

        String originLocation = localData.getString(NavigationPrompt.NAV_ORIGIN, null);
        String destinationLocation = localData.getString(NavigationPrompt.NAV_DESTINATION, null);
        LatLng waypoint = null;

        do {
            try {
                directionJSONObject = new ServiceRequest().
                        execute(DirectionURLGenerator.generateURL(originLocation, destinationLocation, waypoint)).get();

                String[] cardinalBounds = drawDirectionPath(directionJSONObject);

                trafficJSONObject = new ServiceRequest().execute(TrafficURLGenerator.generateURL(cardinalBounds, new String[]{"3", "4"}, new String[]{"1", "2", "6", "7", "8", "10", "11"})).get();
                //trafficJSONObject = new ServiceRequest().execute(TEST_JSON_URL).get();

                List<JSONObject> incidents = checkForIncidents(trafficJSONObject);
                if (incidents != null){
                    int[] hinderingIncident = findIncidentAlongRoute(incidents);
                    if (hinderingIncident[0] != currentRoute.size())
                        waypoint = selectDivertingWaypoint(hinderingIncident);
                }
            } catch (InterruptedException e) {
                System.out.println("ERROR : Current thread was interrupted");
            } catch (ExecutionException e) {
                System.out.println("ERROR: AsyncTask was unable to execute successfully.");
            } catch (JSONException e){
                System.out.println("ERROR: Invalid traffic JSON object");
            }
        } while (waypoint != null);
    }

    private String[] drawDirectionPath(JSONObject directionJSONObject){

        if (directionJSONObject == null) return null;
        if (!directionJSONObject.opt("status").equals("OK")) return null;

        int FIRST_ROUTE_POSITION = 0;
        int FIRST_LEG_POSITION = 0;

        JSONArray routes = directionJSONObject.optJSONArray("routes");
        JSONObject firstRoute = routes.optJSONObject(FIRST_ROUTE_POSITION);

        JSONObject smoothPolyline = firstRoute.optJSONObject("overview_polyline");
        String polylinePoints = (String)smoothPolyline.opt("points");

        System.out.println("Polyline points : \n" + polylinePoints);

        JSONArray legs = firstRoute.optJSONArray("legs");
        JSONObject firstLeg = legs.optJSONObject(FIRST_LEG_POSITION);

        JSONArray steps = firstLeg.optJSONArray("steps");
        PolylineOptions routeLineOptions = new PolylineOptions();

        List<LatLng> polylinegLatLng = decodePoly(polylinePoints);

        currentRoute.clear();
        for(LatLng polyline : polylinegLatLng) currentRoute.add(polyline);

        LatLng firstPolylineLatLng = polylinegLatLng.get(0);

        double NLat = firstPolylineLatLng.latitude,
               SLat = firstPolylineLatLng.latitude,
               ELng = firstPolylineLatLng.longitude,
               WLng = firstPolylineLatLng.longitude;

        for (LatLng polylineLatLngPoint : polylinegLatLng){

            double currentLng = polylineLatLngPoint.longitude;
            double currentLat = polylineLatLngPoint.latitude;

            if (currentLat < SLat) SLat = currentLat;
            else if (currentLat > NLat) NLat = currentLat;

            if (currentLng < WLng) WLng = currentLng;
            else if (currentLng > ELng) ELng = currentLng;

            routeLineOptions.add(polylineLatLngPoint);
        }

        navigationMap.addPolyline(routeLineOptions);
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

    private List<JSONObject> checkForIncidents(JSONObject trafficJSONObjcet) throws JSONException {

        List<JSONObject> incidentList = new ArrayList<JSONObject>();

        JSONArray resourceSets = trafficJSONObjcet.optJSONArray("resourceSets");
        JSONArray resourceSet = resourceSets.optJSONArray(RESOURCE_SET_POSITION);

        for (int i = 0; i < resourceSet.length(); i++){
            JSONObject incident = resourceSet.optJSONObject(i);
            incidentList.add(incident);
        }
        return incidentList;
    }

    private int[] findIncidentAlongRoute(List<JSONObject> incidents){

        int closestStartIndex = currentRoute.size();
        int closestEndIndex = -1;

        for (JSONObject incident : incidents) {

            JSONObject startPoint = incident.optJSONObject("point");
            JSONObject endPoint = incident.optJSONObject("toPoint");

            String startPointCoordinates = startPoint.optString("coordinates");
            String endPointCoordinates = "";
            if (endPoint != null ) endPointCoordinates = endPoint.optString("coordinates");

            LatLng incidentStartPoint = extractCoordinates(startPointCoordinates);
            LatLng incidentEndPoint = null;
            if (!endPointCoordinates.equals("")) incidentEndPoint = extractCoordinates(endPointCoordinates);

            if (currentRoute.contains(incidentStartPoint))
                for (int j = 0; j < currentRoute.size(); j++)
                    if (currentRoute.get(j).longitude == incidentStartPoint.longitude && currentRoute.get(j).latitude == incidentStartPoint.latitude)
                        if (j < closestStartIndex) closestStartIndex = j;

            if (endPoint != null && incidentEndPoint != null)
                for (int j = 0; j < currentRoute.size(); j++)
                    if (currentRoute.get(j).longitude == incidentEndPoint.longitude && currentRoute.get(j).latitude == incidentEndPoint.latitude)
                        if (j > closestEndIndex && j > closestStartIndex) closestStartIndex = j;

        }
        return new int[] {closestStartIndex, closestEndIndex};
    }

    private LatLng extractCoordinates(String coordinates){
        int LAT = 0, LNG = 1;

        String[] coordinateLiterals = coordinates.split(",");
        for(String coordinate : coordinateLiterals) coordinate.trim();

        return new LatLng(Double.valueOf(coordinateLiterals[LAT]), Double.valueOf(coordinateLiterals[LNG]));
    }

    private LatLng selectDivertingWaypoint(int[] incidentPointIndices){

        double FIVE_PERCENT = .2;

        LatLng destination = currentRoute.get(currentRoute.size() - 1);
        LatLng incidentStart = currentRoute.get(incidentPointIndices[0]);
        LatLng incidentEnd;

        if (incidentPointIndices.length > 1) incidentEnd = currentRoute.get(incidentPointIndices[1]);

        double newLat = 0.0, newLng = 0.0;
        String direction = "";

        if (destination.latitude >= incidentStart.latitude) direction = direction.concat("N");
        else if (destination.latitude < incidentStart.latitude) direction = direction.concat("S");

        if(destination.longitude >= incidentStart.longitude) direction = direction.concat("E");
        else if (destination.longitude < incidentStart.longitude) direction = direction.concat("W");

        double lngDifference = Math.abs(destination.longitude - incidentStart.longitude);
        double latDifference = Math.abs(destination.latitude - incidentStart.latitude);

        if (direction.equals("NW")){
            newLat = (incidentStart.latitude - (latDifference * FIVE_PERCENT));
            newLng = (incidentStart.longitude + (lngDifference * FIVE_PERCENT));
        } else if (direction.equals("NE")){
            newLat = (incidentStart.latitude + (latDifference * FIVE_PERCENT));
            newLng = (incidentStart.longitude + (lngDifference * FIVE_PERCENT));
        } else if (direction.equals("SW")){
            newLat = (incidentStart.latitude - (latDifference * FIVE_PERCENT));
            newLng = (incidentStart.longitude - (lngDifference * FIVE_PERCENT));
        } else if (direction.equals("SE")){
            newLat = (incidentStart.latitude + (latDifference * FIVE_PERCENT));
            newLng = (incidentStart.longitude - (lngDifference * FIVE_PERCENT));
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
