package ApplicationListAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/13/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListItem {

    private final String applicationName;
    private final int icon;

    public ApplicationListItem(String applicationName, int icon){
        this.applicationName = applicationName;
        this.icon = icon;
    }

    public String getApplicationName(){
        return applicationName;
    }
}
