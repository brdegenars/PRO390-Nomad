package com.activites;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        destinationInput = (EditText)this.findViewById(R.id.destinationInput);

        Button navigateMe = (Button)this.findViewById(R.id.navigateMe);
        navigateMe.setOnClickListener(navigateMeOnClickListener);
        // navigateMe.setEnabled(false);
    }

    private View.OnClickListener navigateMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Take origin and destination, send to Google direction service, display map.
            originInput.getText();
        }
    };
}
