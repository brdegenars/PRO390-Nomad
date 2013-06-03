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
import org.json.JSONObject;
import service.ServiceRequest;
import service.DirectionURLGenerator;
import service.TrafficURLGenerator;

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

    private final int FIRST_ROUTE_POSITION = 0;
    private final int FIRST_LEG_POSITION = 0;

    private GoogleMap navigationMap;

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

        JSONObject directionJSONObject;

        try {
            directionJSONObject = new ServiceRequest().execute(DirectionURLGenerator.
                    generateURL(localData.getString(NavigationPrompt.NAV_ORIGIN, null), localData.getString(NavigationPrompt.NAV_DESTINATION, null))).get();

            String[] cardinalBounds = drawDirectionPath(directionJSONObject);

            new ServiceRequest().execute(TrafficURLGenerator.generateURL(cardinalBounds, new String[]{"3", "4"}, new String[]{"1", "2", "6", "7", "8", "10", "11"}));

        } catch (InterruptedException e) {
            System.out.println("ERROR : Current thread was interrupted");
        } catch (ExecutionException e) {
            System.out.println("ERROR: AsyncTask was unable to execute successfully.");
        }
    }

    private String[] drawDirectionPath(JSONObject directionJSONObject){

        if (directionJSONObject == null) return null;
        if (!directionJSONObject.opt("status").equals("OK")) return null;

        JSONArray routes = directionJSONObject.optJSONArray("routes");
        JSONObject firstRoute = routes.optJSONObject(FIRST_ROUTE_POSITION);

        JSONObject smoothPolyline = firstRoute.optJSONObject("overview_polyline");
        String polylinePoints = (String)smoothPolyline.opt("points");

        System.out.println("Polyline points : \n" + polylinePoints);


        JSONArray legs = firstRoute.optJSONArray("legs");
        JSONObject firstLeg = legs.optJSONObject(FIRST_LEG_POSITION);

        JSONArray steps = firstLeg.optJSONArray("steps");
        PolylineOptions routeLineOptions = new PolylineOptions();
        //LatLng originLatLng;

//        for (int i = 0; i < steps.length(); i++){
//
//            JSONObject stepPolyline = steps.optJSONObject(3);
//            List<LatLng> stepPolylinePoints = decodePoly(stepPolyline.optString("points"));
//
//            for (LatLng stepPolylinePoint : stepPolylinePoints){
//                //LatLng geoPointLatLng = new LatLng((double)stepPolylinePoint.getLatitudeE6(), (double)stepPolylinePoint.getLongitudeE6());
//                routeLineOptions.add(stepPolylinePoint);
//            }
//        }

//        for (int i = 0; i < steps.length(); i++){
//            System.out.println("Start location for step " + i + ": " + steps.optJSONObject(i).opt("start_location"));
//            System.out.println("End location for step " + i + ": " + steps.optJSONObject(i).opt("end_location"));
//
//            JSONObject startLocation = (JSONObject)steps.optJSONObject(i).opt("start_location");
//            JSONObject endLocation = (JSONObject)steps.optJSONObject(i).opt("end_location");
//
//            LatLng start = new LatLng((Double)startLocation.opt("lat"), (Double)startLocation.opt("lng"));
//            LatLng end = new LatLng((Double)endLocation.opt("lat"), (Double)endLocation.opt("lng"));
//
//            if (i == 0) originLatLng = new LatLng((Double)startLocation.opt("lat"), (Double)startLocation.opt("lng"));
//
//
//            routeLineOptions.add(start);
//            routeLineOptions.add(end);
//
//            points.add(start);
//            points.add(end);
//        }

        List<LatLng> polylingLatLng = decodePoly(polylinePoints);
        LatLng firstPolylineLatLng = polylingLatLng.get(0);

        double NLat = firstPolylineLatLng.latitude,
               SLat = firstPolylineLatLng.latitude,
               ELng = firstPolylineLatLng.longitude,
               WLng = firstPolylineLatLng.longitude;

        for (LatLng polylineLatLngPoint : polylingLatLng){

            double currentLng = polylineLatLngPoint.longitude;
            double currentLat = polylineLatLngPoint.latitude;

            if (currentLat < SLat) SLat = currentLat;
            else if (currentLat > NLat) NLat = currentLat;

            if (currentLng < ELng) ELng = currentLng;
            else if (currentLng > WLng) WLng = currentLng;

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
