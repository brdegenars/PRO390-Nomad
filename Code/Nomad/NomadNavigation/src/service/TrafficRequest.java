package service;

import android.os.AsyncTask;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/31/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrafficRequest extends AsyncTask<String, String, JSONObject> {

    private final String URL ="http://dev.virtualearth.net/REST/v1/Traffic/Incidents/";

    @Override
    protected JSONObject doInBackground(String... params) {
        return null;
    }
}
