package com.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private View.OnClickListener navigateMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //String originValue = originInput.getText().toString();
            //String destinationValue = destinationInput.getText().toString();

            String originValue = "884 Rocky Mouth Lane Draper, UT 84020";
            String destinationValue = "531 East 2700 South";

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
