package subject;

import android.content.Context;
import android.widget.EditText;
import observer.LocationTextObserver;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/17/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationTextSubject extends EditText{

    private EditText locationInput;

    private ArrayList<LocationTextObserver> observers = new ArrayList<LocationTextObserver>();

    public LocationTextSubject(Context context, EditText locationInput){
        super(context);
        this.locationInput = locationInput;
    }

    private LocationTextSubject(Context context) {
        super(context);
    }

    public void registerObserver(LocationTextObserver observer){
        observers.add(observer);
    }

    public void unregisterObserver(LocationTextObserver observer){
        if (observers.contains(observer)) observers.remove(observer);
    }

    public void notifyAllObservers(){
        for( LocationTextObserver observer : observers)
            observer.update();
    }
}
