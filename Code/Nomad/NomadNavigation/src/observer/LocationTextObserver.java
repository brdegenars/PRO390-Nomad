package observer;

import android.widget.Button;
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
    private Button componentAffected;

    public LocationTextObserver(EditText origin, EditText destination, Button componentAffected){
        this.originInput = origin;
        this.destinationInput = destination;
        this.componentAffected = componentAffected;
    }

    public void update(){

        if (originInput.getText().length() < 1 || destinationInput.getText().length() < 1){
            componentAffected.setEnabled(false);
        } else {
            componentAffected.setEnabled(true);
        }

    }

}
