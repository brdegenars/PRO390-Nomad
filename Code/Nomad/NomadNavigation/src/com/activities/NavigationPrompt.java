package com.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import observer.LocationTextObserver;
import subject.LocationTextSubject;

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

    private View.OnClickListener navigateMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Take origin and destination, send to Google direction service, display map.
            System.out.println("INPUTS: ");
            System.out.println("Origin : " + originInput.getText());
            System.out.println("Destination : " + destinationInput.getText());

            SharedPreferences localData = getPreferences(Activity.MODE_PRIVATE);
            SharedPreferences.Editor localDataEditor = localData.edit();

            localDataEditor.putString("origin", originInput.getText().toString());
            localDataEditor.putString("destination", destinationInput.getText().toString());

            Intent launchMapIntent = new Intent(v.getContext(), MapHandler.class);
            startActivity(launchMapIntent);
        }
    };
}
