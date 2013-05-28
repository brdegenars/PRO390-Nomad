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

    private EditText originInput, destinationInput;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        originInput = (EditText)this.findViewById(R.id.originInput);
        originInput.setText("Origin");
        LocationTextSubject originSubject = new LocationTextSubject(originInput);

        destinationInput = (EditText)this.findViewById(R.id.destinationInput);
        destinationInput.setText("Destination");
        LocationTextSubject destinationSubject = new LocationTextSubject(destinationInput);

        Button navigateMe = (Button) this.findViewById(R.id.navigateMe);
        navigateMe.setOnClickListener(navigateMeOnClickListener);
        //navigateMe.setEnabled(false);

        LocationTextObserver locationTextObserver = new LocationTextObserver(originInput, destinationInput, navigateMe);

        originSubject.registerObserver(locationTextObserver);
        destinationSubject.registerObserver(locationTextObserver);
    }

    @Override
    public void onResume(){

    }

    @Override
    public void onPause(){

    }

    private View.OnClickListener navigateMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String originValue = originInput.getText().toString();
            String destiniationValue = destinationInput.getText().toString();

            System.out.println("INPUTS: ");
            System.out.println("Origin : " + originValue);
            System.out.println("Destination : " + destiniationValue);

            SharedPreferences localData = getPreferences(Activity.MODE_PRIVATE);
            SharedPreferences.Editor localDataEditor = localData.edit();

            localDataEditor.putString("origin", originValue);
            localDataEditor.putString("destination", destiniationValue);

            DirectionRequest sendRequest = new DirectionRequest();
            JSONObject directionJSONObject;

            try {
                directionJSONObject = sendRequest.execute(DirectionURLGenerator.generateURL(originValue, destiniationValue)).get();
            } catch (InterruptedException e) {
                System.out.println("ERROR : Current thread was interrupted");
            } catch (ExecutionException e) {
                System.out.println("ERROR: AsyncTask was unable to execute successfully.");
            }

            // TODO: Take directionJSONObject and send it off to be constructed into a map with directions from the JSONObject
            Intent launchMapIntent = new Intent(v.getContext(), MapHandler.class);
            startActivity(launchMapIntent);
        }
    };
}
