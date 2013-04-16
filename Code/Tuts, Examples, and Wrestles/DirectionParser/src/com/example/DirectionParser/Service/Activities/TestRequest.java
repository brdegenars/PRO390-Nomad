package com.example.DirectionParser.Service.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.DirectionParser.R;
import com.example.DirectionParser.Service.Service.ServiceRequest;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/15/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRequest extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ServiceRequest serviceRequest = new ServiceRequest();

        Button testButton = (Button)this.findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceRequest.request("Toronto","Boston",false, null, null, "metric");
            }
        });
    }
}