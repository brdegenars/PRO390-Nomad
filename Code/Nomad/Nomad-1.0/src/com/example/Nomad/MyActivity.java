package com.example.Nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        topLeft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent.createChooser(new Intent(Intent.ACTION_CHOOSER), "Select Application");
                return true;
            }
        });
    }
}
