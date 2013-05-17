package Components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/16/13
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Hotpad extends ImageButton{

    private ImageButton hotpad;
    private String packageName;

    public Hotpad(Context context, ImageButton hotpad) {
        super(context);

        this.hotpad = hotpad;
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

    public void setApplication(String packageName){
        this.packageName = packageName;
        hotpad.setOnClickListener(executeBoundApplication);
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
