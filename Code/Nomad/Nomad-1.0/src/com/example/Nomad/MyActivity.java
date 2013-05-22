package com.example.Nomad;

import ApplicationListAdapter.ApplicationListAdapter;
import ApplicationListAdapter.ApplicationListItem;
import Components.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyActivity extends Activity
{
    private final int NUMBER_OF_HOTPADS = 6;

    private LocationManager locationManager;
    private TextView address, cityStateZip;
    private Context activityContext;
    private Hotpad currentlySelectedHotPad;

    private HashMap<ImageButton, Hotpad> hotPads = new HashMap<>(NUMBER_OF_HOTPADS);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        activityContext = this.getApplicationContext();
        locationManager  = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        // Creation and instantiation of Hot pads (ImageButtons)
        initializeHotPads();

        // Instantiation of TextViews
        address = (TextView)this.findViewById(R.id.textView_home_address);
        cityStateZip = (TextView)this.findViewById(R.id.textView_home_cityStateZip);

        // Register listener with locationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void initializeHotPads() {
        ImageButton topLeft, topCenter, topRight, bottomLeft, bottomCenter, bottomRight;

        topLeft = (ImageButton)this.findViewById(R.id.imageButton_home_topLeft);
        hotPads.put(topLeft, new Hotpad(this, topLeft));

        topCenter = (ImageButton)this.findViewById(R.id.imageButton_home_topCenter);
        hotPads.put(topCenter, new Hotpad(this, topCenter));

        topRight = (ImageButton)this.findViewById(R.id.imageButton_home_topRight);
        hotPads.put(topRight, new Hotpad(this, topRight));

        bottomLeft = (ImageButton)this.findViewById(R.id.imageButton_home_bottomLeft);
        hotPads.put(bottomLeft, new Hotpad(this, bottomLeft));

        bottomCenter = (ImageButton)this.findViewById(R.id.imageButton_home_bottomCenter);
        hotPads.put(bottomCenter, new Hotpad(this, bottomCenter));

        bottomRight = (ImageButton)this.findViewById(R.id.imageButton_home_bottomRight);
        hotPads.put(bottomRight, new Hotpad(this, bottomRight));

        // Binds the onLongClickListener to each hot pad
        topLeft.setOnLongClickListener(onLongClickListener);
        topCenter.setOnLongClickListener(onLongClickListener);
        topRight.setOnLongClickListener(onLongClickListener);

        bottomLeft.setOnLongClickListener(onLongClickListener);
        bottomCenter.setOnLongClickListener(onLongClickListener);
        bottomRight.setOnLongClickListener(onLongClickListener);
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // Stores the hot pad that fired the event
            ImageButton hotPadImageButton = (ImageButton)v;
            currentlySelectedHotPad = hotPads.get(hotPadImageButton);

            PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);

            // List of apps we care about
            ArrayList<ApplicationInfo> nonSystemApplications = new ArrayList<>();

            // Find the app we care about
            for (ApplicationInfo applicationInfo : installedApplications){
                // TODO: These flag filers currently do not work as expected.
                // Apps like G-mail and maps are updated system apps, I want those.
                System.out.println(applicationInfo.name + "'s Flags : " + applicationInfo.flags);
                if (applicationInfo.flags == ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)
                    nonSystemApplications.add(applicationInfo);
                    // Any other applications that are non system applications
                else if (applicationInfo.flags != ApplicationInfo.FLAG_SYSTEM)
                    nonSystemApplications.add(applicationInfo);
            }

            // Tests to see if any applications were filtered by the flag filter process above
            System.out.println("Is the same collection: " + installedApplications.containsAll(nonSystemApplications));

            // Collections holding application names and icons
            ApplicationListItem[] applicationListItems = new ApplicationListItem[nonSystemApplications.size()];

            int appCount = 0;
            // Add names and icons of non system applications to respective collections for display
            for (ApplicationInfo applicationInfo : nonSystemApplications){
                CharSequence applicationLabel = (packageManager.getApplicationLabel(applicationInfo));
                String applicationPackageName = (applicationInfo.packageName);
                Drawable applicationIcon = (packageManager.getApplicationIcon(applicationInfo));

                applicationListItems[appCount++] = new ApplicationListItem(applicationLabel, applicationPackageName, applicationIcon);
            }

            AlertDialog.Builder appListDialogBuilder = new AlertDialog.Builder(v.getContext());
            appListDialogBuilder.setTitle("Pick One");

            ApplicationListAdapter applicationListAdapter = new ApplicationListAdapter(v.getContext(), R.layout.applicationlistitem, applicationListItems);

            appListDialogBuilder.setAdapter(applicationListAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView applicationListView = ((AlertDialog)dialog).getListView();
                    ApplicationListItem itemSelected = (ApplicationListItem)applicationListView.getAdapter().getItem(which);

                    System.out.println("SELECTED APPLICATION : " + itemSelected.getApplicationName());
                    // TODO: Enable the ability to retain the application icon on each hotpad upon rotation of the screen.
                    // May require building two layouts for each orientation of the screen.
                    // RemoteViews newRemoteView = new RemoteViews(getPackageName(), R.layout.main);

                    applicationListView.getAdapter().getItem(which);
                    // Sets image for hot pad that fired the original event
                    currentlySelectedHotPad.setApplication(itemSelected.getPackageName());
                    currentlySelectedHotPad.getImageButton().setImageDrawable(itemSelected.getImageResourceDrawable());
                }
            });

            appListDialogBuilder.show();
            return true;
        }
    };

    LocationListener locationListener = new LocationListener() {

        Geocoder addressGeocoder;
        List<Address> addresses;

        @Override
        public void onLocationChanged(Location location) {

            addressGeocoder = new Geocoder(activityContext);

            try{
                addresses = addressGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e){
                System.out.println("SOMETHING WENT WRONG WITH GETTING THE ADDRESS FROM GEOCODER");
            }

            /* Street address
               City, State Code 6 digit zip
               Country code
             */
            address.setText(addresses.get(0).getAddressLine(0));
            cityStateZip.setText(addresses.get(0).getAddressLine(1));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };
}