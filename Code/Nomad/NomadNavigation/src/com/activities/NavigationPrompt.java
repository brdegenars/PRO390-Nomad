package com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import observer.LocationTextObserver;
import subject.LocationTextSubject;

public class NavigationPrompt extends Activity {

    public static final String NAV_PREFERENCES = "navPreferences";
    public static final String NAV_ORIGIN = "origin";
    public static final String NAV_DESTINATION = "destination";

    public String currentLat, currentLong, currentAddress;

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

        //Geocoder geocoder = new Geocoder(getBaseContext());
        @Override
        public void onLocationChanged(Location location) {
            currentLat = String.valueOf(location.getLatitude());
            currentLong = String.valueOf(location.getLongitude());

            // TODO: Display address instead of lat and long
            /*List<Address> currentAddresses = null;
            try{
            currentAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e){
                System.out.println("ERROR : Bad location");
            }

            currentAddress = currentAddresses.get(0).getAddressLine(0).toString(); */
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
        //String originValue = originInput.getText().toString();
        //String destinationValue = destinationInput.getText().toString();

        String originValue = "10701 S. RiverFront Parkway South Jordan, Utah 84095";
        String destinationValue = "E 5290 S Salt Lake City, UT 84111";

//        String originValue = "40.558450, -111.910162";
//        String destinationValue = "40.558857, -111.909358";

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
