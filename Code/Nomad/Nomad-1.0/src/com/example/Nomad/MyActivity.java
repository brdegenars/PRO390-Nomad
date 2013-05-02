package com.example.Nomad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
            List appList = getPackageManager().queryIntentActivities(intent, 0);

            AlertDialog.Builder appListDialogBuilder = new AlertDialog.Builder(getApplicationContext());
            appListDialogBuilder.setTitle("Choose Application");
            // TODO: Fix this red stuff, it doesn't like the anonymous listener for some reason
            appListDialogBuilder.setItems(, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
//
//            Intent listOfAppsIntent = Intent.createChooser(intent, "Select Application");
//            listOfAppsIntent.
//            startActivityForResult(listOfAppsIntent, 0);
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
