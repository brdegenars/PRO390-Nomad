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

            Intent intent = new Intent(Intent.ACTION_CHOOSER);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List appList = getPackageManager().queryIntentActivities(intent, 0);

            CharSequence[] appCharSequence = (CharSequence[])appList.toArray(new CharSequence[appList.size()]);

            AlertDialog.Builder appListDialogBuilder = new AlertDialog.Builder(v.getContext());
            appListDialogBuilder.setTitle("Choose Application");

            // TODO: This char sequence appCharSequence doesn't show the apps the way I'd like.
            appListDialogBuilder.setItems(appCharSequence, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: Work out the logic for what I'd like to do with the app that they select from the list
                }
            });

            appListDialogBuilder.show();
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
