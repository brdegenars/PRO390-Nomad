package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import observer.LocationTextObserver;
import org.json.JSONObject;
import service.DirectionRequest;
import service.DirectionURLGenerator;
import subject.LocationTextSubject;

import java.util.concurrent.ExecutionException;

public class NavigationPrompt extends Activity {

    public static final String NAV_PREFERENCES = "navPreferences";
    public static final String NAV_ORIGIN = "origin";
    public static final String NAV_DESTINATION = "destination";

    public String currentLat, currentLong;

    private EditText originInput, destinationInput;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        originInput = (EditText)this.findViewById(R.id.originInput);
        LocationTextSubject originSubject = new LocationTextSubject(originInput);

        destinationInput = (EditText)this.findViewById(R.id.destinationInput);
        LocationTextSubject destinationSubject = new LocationTextSubject(destinationInput);

        Button navigateMe = (Button) this.findViewById(R.id.navigateMe);
        navigateMe.setOnClickListener(navigateMeOnClickListener);
        //navigateMe.setEnabled(false);

        LocationTextObserver locationInputObserver = new LocationTextObserver(originInput, destinationInput, navigateMe);

        originSubject.registerObserver(locationInputObserver);
        destinationSubject.registerObserver(locationInputObserver);

        ImageButton originMyLocationSelector = (ImageButton)this.findViewById(R.id.originMapSelection);
        ImageButton destinationMyLocationSelector = (ImageButton)this.findViewById(R.id.destinationMapSelection);

        originMyLocationSelector.setOnClickListener(myOriginSelectorOnClickListener);
        destinationMyLocationSelector.setOnClickListener(myDestinationSelectorOnClickListener);

        LocationManager locationManager  = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, currentLocationListener);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private ImageButton.OnClickListener myOriginSelectorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            originInput.setText(currentLat + "," + currentLong);
        }
    };

    private ImageButton.OnClickListener myDestinationSelectorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            destinationInput.setText(currentLat + "," + currentLong);
        }
    };

    private LocationListener currentLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLat = String.valueOf(location.getLatitude());
            currentLong = String.valueOf(location.getLongitude());

            // TODO: Display address instead of lat and long
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    private View.OnClickListener navigateMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String originValue = originInput.getText().toString();
            String destinationValue = destinationInput.getText().toString();

            //String originValue = "884 Rocky Mouth Lane Draper, UT 84020";
            //String destinationValue = "531 East 2700 South";

            originValue = originValue.replace(' ', '+');
            destinationValue = destinationValue.replace(' ', '+');

            System.out.println("INPUTS: ");
            System.out.println("Origin : " + originValue);
            System.out.println("Destination : " + destinationValue);

            SharedPreferences localData = getSharedPreferences(NAV_PREFERENCES, Activity.MODE_PRIVATE);
            SharedPreferences.Editor localDataEditor = localData.edit();

            localDataEditor.putString(NAV_ORIGIN, originValue);
            localDataEditor.putString(NAV_DESTINATION, destinationValue);
            localDataEditor.commit();

            Intent launchMapIntent = new Intent(v.getContext(), MapHandler.class);
            startActivity(launchMapIntent);

        }
    };
}
