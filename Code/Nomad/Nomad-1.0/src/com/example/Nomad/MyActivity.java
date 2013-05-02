package com.example.Nomad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

public class MyActivity extends Activity
{
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            /* TODO: This is where the logic for displaying the list of available apps for hot pads is
               TODO: work on this...
             */

            Intent intent = new Intent(Intent.ACTION_CHOOSER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            List appList = getPackageManager().queryIntentActivities(intent, 0);

            Intent listOfAppsIntent = Intent.createChooser(intent, "Select Application");
            startActivityForResult(listOfAppsIntent, 0);
            return true;
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Creation and instantiation of ImageButtons (hot pads)
        ImageButton topLeft, topCenter, topRight, bottomLeft, bottomCenter, bottomRight;

        topLeft = (ImageButton)this.findViewById(R.id.imageButton_home_topLeft);
        topCenter = (ImageButton)this.findViewById(R.id.imageButton_home_topCenter);
        topRight = (ImageButton)this.findViewById(R.id.imageButton_home_topRight);

        bottomLeft = (ImageButton)this.findViewById(R.id.imageButton_home_bottomLeft);
        bottomCenter = (ImageButton)this.findViewById(R.id.imageButton_home_bottomCenter);
        bottomRight = (ImageButton)this.findViewById(R.id.imageButton_home_bottomRight);

        // Binds the above onLongClickListener to each hot pad
        topLeft.setOnLongClickListener(onLongClickListener);
        topCenter.setOnLongClickListener(onLongClickListener);
        topRight.setOnLongClickListener(onLongClickListener);

        bottomLeft.setOnLongClickListener(onLongClickListener);
        bottomCenter.setOnLongClickListener(onLongClickListener);
        bottomRight.setOnLongClickListener(onLongClickListener);
    }


}
