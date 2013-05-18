package subject;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
public class LocationTextSubject{

    private ArrayList<LocationTextObserver> observers = new ArrayList<LocationTextObserver>();

    public LocationTextSubject(EditText locationInput){
        TextWatcher locationTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notifyAllObservers();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        locationInput.addTextChangedListener(locationTextWatcher);
    }


    public void registerObserver(LocationTextObserver observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for( LocationTextObserver observer : observers)
            observer.update();
    }

}
