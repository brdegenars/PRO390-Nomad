package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import service.DirectionRequest;
import service.DirectionURLGenerator;

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
    private final int LEG_POSITION = 3;
    private final int STEP_POSITION = 7;
    private final int START_LOCATION_POSITION = 5;
    private final int END_LOCATION_POSITION = 2;

    private GoogleMap navigationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);

        LocationManager locationManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, currentLocationListener);
    }

    @Override
    protected void onStart(){
        super.onStart();
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

        DirectionRequest sendRequest = new DirectionRequest();
        JSONObject directionJSONObject = null;

        try {
            directionJSONObject = sendRequest.execute(DirectionURLGenerator.
                    generateURL(localData.getString(NavigationPrompt.NAV_ORIGIN, null), localData.getString(NavigationPrompt.NAV_DESTINATION, null))).get();
        } catch (InterruptedException e) {
            System.out.println("ERROR : Current thread was interrupted");
        } catch (ExecutionException e) {
            System.out.println("ERROR: AsyncTask was unable to execute successfully.");
        }

        drawDirectionPath(directionJSONObject);
    }

    private void drawDirectionPath(JSONObject directionJSONObject){

        if (directionJSONObject == null) return;
        if (!directionJSONObject.opt("status").equals("OK")) return;

        JSONArray routes = directionJSONObject.optJSONArray("routes");
        JSONObject firstRoute = routes.optJSONObject(FIRST_ROUTE_POSITION);

        JSONArray legs = firstRoute.optJSONArray("legs");
        JSONObject firstLeg = legs.optJSONObject(FIRST_LEG_POSITION);

        JSONArray steps = firstLeg.optJSONArray("steps");

        PolylineOptions routeLineOptions = new PolylineOptions();
        LatLng originLatLng = null;
        for (int i = 0; i < steps.length(); i++){
            System.out.println("Start location for step " + i + ": " + steps.optJSONObject(i).opt("start_location"));
            System.out.println("End location for step " + i + ": " + steps.optJSONObject(i).opt("end_location"));

            JSONObject startLocation = (JSONObject)steps.optJSONObject(i).opt("start_location");
            JSONObject endLocation = (JSONObject)steps.optJSONObject(i).opt("end_location");

            if (i == 0) originLatLng = new LatLng((Double)startLocation.opt("lat"), (Double)startLocation.opt("lng"));

            routeLineOptions.add(new LatLng((Double)startLocation.opt("lat"), (Double)startLocation.opt("lng")));
            routeLineOptions.add(new LatLng((Double)endLocation.opt("lat"), (Double)endLocation.opt("lng")));
        }
        navigationMap.addPolyline(routeLineOptions);
        animateToHere(originLatLng);
        // TODO: Finish parsing through the JSON Array's and Objects to obtain necessary information to draw the route on the map.

    }

    @Override
    protected void onPause(){
        super.onPause();
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
