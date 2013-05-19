package ApplicationListAdapter;

import android.graphics.drawable.Drawable;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/13/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListItem {

    private final CharSequence applicationName;
    private final String packageName;
    private final Drawable icon;

    public ApplicationListItem(CharSequence applicationName, String packageName, Drawable icon){
        this.applicationName = applicationName;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getPackageName(){
        return packageName.toString();
    }

    public CharSequence getApplicationName(){
        return applicationName;
    }

    public Drawable getImageResourceDrawable(){
        return icon;
    }
}