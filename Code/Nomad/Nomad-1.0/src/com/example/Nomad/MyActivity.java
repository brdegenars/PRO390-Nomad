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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyActivity extends Activity
{
    private final int NUMBER_OF_HOTPADS = 6;
    private final int TOP_LEFT = 0;
    private final int TOP_CENTER = 1;
    private final int TOP_RIGHT = 2;
    private final int BOTTOM_LEFT = 3;
    private final int BOTTOM_CENTER = 4;
    private final int BOTTOM_RIGHT = 5;

    private TextView address, cityStateZip;
    private Context activityContext = null;
    private Hotpad currentlySelectedHotPad = null;
    private AlertDialog.Builder appListDialogBuilder = null;
    private ApplicationListItem[] applicationListItems = null;

    private HashMap<Integer, Hotpad> hotPads = new HashMap<>(NUMBER_OF_HOTPADS);
    private ImageButton[] imageButtons = new ImageButton[NUMBER_OF_HOTPADS];

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        activityContext = this.getApplicationContext();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Creation and instantiation of Hot pads (ImageButtons)
        initializeHotPads(savedInstanceState);

        // Instantiation of TextViews
        address = (TextView)this.findViewById(R.id.textView_home_address);
        cityStateZip = (TextView)this.findViewById(R.id.textView_home_cityStateZip);

        // Register listener with locationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    protected void onSaveInstanceState(final Bundle savedInstance){

        Hotpad[] persistentHotpads = new Hotpad[NUMBER_OF_HOTPADS];

        for(int i = TOP_LEFT; i <= BOTTOM_RIGHT; i++) persistentHotpads[i] = hotPads.get(i);

        savedInstance.putSerializable("hotpads", persistentHotpads);
    }

    @SuppressWarnings("unchecked")
    private void initializeHotPads(final Bundle savedInstanceState) {
        ImageButton topLeft, topCenter, topRight, bottomLeft, bottomCenter, bottomRight;

        topLeft = (ImageButton)this.findViewById(R.id.imageButton_home_topLeft);
        topCenter = (ImageButton)this.findViewById(R.id.imageButton_home_topCenter);
        topRight = (ImageButton)this.findViewById(R.id.imageButton_home_topRight);
        bottomLeft = (ImageButton)this.findViewById(R.id.imageButton_home_bottomLeft);
        bottomCenter = (ImageButton)this.findViewById(R.id.imageButton_home_bottomCenter);
        bottomRight = (ImageButton)this.findViewById(R.id.imageButton_home_bottomRight);

        imageButtons[TOP_LEFT] = topLeft;
        imageButtons[TOP_CENTER] = topCenter;
        imageButtons[TOP_RIGHT] = topRight;
        imageButtons[BOTTOM_LEFT] = bottomLeft;
        imageButtons[BOTTOM_CENTER] = bottomCenter;
        imageButtons[BOTTOM_RIGHT] = bottomRight;

        // Binds the onLongClickListener to each hot pad
        for (ImageButton imageButton : imageButtons) imageButton.setOnLongClickListener(onLongClickListener);

        if (savedInstanceState != null){
            Hotpad[] oldHotpadData = ((Hotpad[]) savedInstanceState.getSerializable("hotpads"));

            for (int i = TOP_LEFT; i <= BOTTOM_RIGHT; i++){
                Hotpad tempHotpad = new Hotpad(this, imageButtons[i], i);

                if (oldHotpadData[i].getBoundApplication() != null)
                    tempHotpad.setApplication(oldHotpadData[i].getBoundApplication(), oldHotpadData[i].getApplicationIcon());

                hotPads.put(i, tempHotpad);
            }
        }else for (int i = TOP_LEFT; i <= BOTTOM_RIGHT; i++) hotPads.put(i, new Hotpad(this, imageButtons[i], i));
    }

    private void buildApplicationDialogList(){

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
        applicationListItems = new ApplicationListItem[nonSystemApplications.size()];

        int appCount = 0;
        // Add names and icons of non system applications to respective collections for display
        for (ApplicationInfo applicationInfo : nonSystemApplications){
            CharSequence applicationLabel = (packageManager.getApplicationLabel(applicationInfo));
            String applicationPackageName = (applicationInfo.packageName);
            Drawable applicationIcon = (packageManager.getApplicationIcon(applicationInfo));

            applicationListItems[appCount++] = new ApplicationListItem(applicationLabel, applicationPackageName, applicationIcon);
        }
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

        // Stores the hot pad image button that fired the event
        ImageButton hotPadImageButton = (ImageButton)v;

        int currentHotpadPosition = -1;
        for (int i = TOP_LEFT; i <= BOTTOM_RIGHT; i++) if (imageButtons[i] == hotPadImageButton) currentHotpadPosition = i;

        currentlySelectedHotPad = hotPads.get(currentHotpadPosition);

        if (applicationListItems == null) buildApplicationDialogList();

            appListDialogBuilder = new AlertDialog.Builder(v.getContext());
            appListDialogBuilder.setTitle("Pick One");

            ApplicationListAdapter applicationListAdapter = new ApplicationListAdapter(v.getContext(), R.layout.applicationlistitem, applicationListItems);

            appListDialogBuilder.setAdapter(applicationListAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView applicationListView = ((AlertDialog)dialog).getListView();
                    ApplicationListItem itemSelected = (ApplicationListItem)applicationListView.getAdapter().getItem(which);

                    System.out.println("SELECTED APPLICATION : " + itemSelected.getApplicationName());

                    //applicationListView.getAdapter().getItem(which);
                    // Sets image for hot pad that fired the original event
                    currentlySelectedHotPad.setApplication(itemSelected.getPackageName(), itemSelected.getImageResourceDrawable());
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