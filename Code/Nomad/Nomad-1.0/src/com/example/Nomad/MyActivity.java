package com.example.Nomad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MyActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageButton topLeft = (ImageButton)this.findViewById(R.id.imageButton_home_topLeft);

    }
}
