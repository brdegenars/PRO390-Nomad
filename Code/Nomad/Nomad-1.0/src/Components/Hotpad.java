package Components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/16/13
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Hotpad extends ImageButton implements Serializable{

    private ImageButton hotpad;
    private String packageName;
    private Drawable applicationIcon;
    private int position;

    public Hotpad(Context context, ImageButton hotpad, int position) {
        super(context);

        setHotpad(hotpad);
        this.position = position;
    }

    /* This constructor is required by ImageButton super class, but I don't want anyone using it;
       other pieces of functionality depend on a ImageButton being passed in on creation.
     */
    private Hotpad(Context context){
        super(context);
    }

    public ImageButton getImageButton(){
        return hotpad;
    }

    public String getBoundApplication(){
        return packageName;
    }

    public Drawable getApplicationIcon(){
        return applicationIcon;
    }

    public void setApplication(String packageName, Drawable applicationIcon){
        this.packageName = packageName;
        this.applicationIcon = applicationIcon;
        hotpad.setImageDrawable(applicationIcon);
        hotpad.setOnClickListener(executeBoundApplication);
    }

    public void setHotpad(ImageButton hotpad){
        this.hotpad = hotpad;
    }

    private OnClickListener executeBoundApplication = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity homeScreen = (Activity)v.getContext();
            Intent launchBoundApplicationIntent = homeScreen.getPackageManager().getLaunchIntentForPackage(packageName);

            homeScreen.startActivity(launchBoundApplicationIntent);
        }
    };
}