package observer;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/17/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationTextObserver {

    private EditText originInput, destinationInput;

    public LocationTextObserver(EditText origin, EditText destination){
        this.originInput = origin;
        this.destinationInput = destination;
    }

    public void update(){

        if (originInput.getText().length() < 1 || destinationInput.getText().length() < 1){
            // TODO: disables navigate button
        } else {
            // TODO: enables navigate button
        }

    }

}
