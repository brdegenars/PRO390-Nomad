package com.activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import service.DirectionRequest;
import service.DirectionURLGenerator;


/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/21/13
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapHandler extends Activity {

    private GoogleMap navigationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);

        LocationManager locationManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, currentLocationListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();

        navigationMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapDisplay)).getMap();

        navigationMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        navigationMap.setMyLocationEnabled(true);
        navigationMap.setTrafficEnabled(true);
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
