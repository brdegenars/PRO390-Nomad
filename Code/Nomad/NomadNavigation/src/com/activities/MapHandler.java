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

    private final int LEG_POSITION = 2;
    private final int STEP_POSITION = 1;
    private final int START_LOCATION_POSITION = 2;
    private final int END_LOCATION_POSITION = 3;

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
        JSONArray legs = routes.optJSONArray(LEG_POSITION);
        JSONArray steps = legs.optJSONArray(STEP_POSITION);

        JSONObject stepStartLocation = steps.optJSONObject(START_LOCATION_POSITION);
        JSONObject stepEndLocation = steps.optJSONObject(END_LOCATION_POSITION);

        // TODO: Finish parsing through the JSON Array's and Objects to obtain necessary information to draw the route on the map.
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    private LocationListener currentLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
            //navigationMap.moveCamera(update);
            navigationMap.animateCamera(update);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };


}
