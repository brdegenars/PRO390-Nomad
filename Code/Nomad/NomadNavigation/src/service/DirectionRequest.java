package service;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/28/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectionRequest extends AsyncTask<String, String, JSONObject> {

    private String json;

    @Override
    protected JSONObject doInBackground(String... params) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            System.out.println("Couldn't execute POST");
        }

        if (httpResponse == null) {
            System.out.println("Null response.");
            return null;
        }

        StatusLine statusLine = httpResponse.getStatusLine();
        System.out.println("Status code : " + statusLine);

        HttpEntity httpEntity = httpResponse.getEntity();

        System.out.println("Content type : " + httpEntity.getContentType());
        System.out.println("Content length : " + httpEntity.getContentLength());

        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            System.out.println("Failed to retrieve content from input stream.");
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String currentLine;
        StringBuilder jsonBuilder = new StringBuilder();
        JSONObject returnJSON = null;

        try {
            while ((currentLine = bufferedReader.readLine()) != null){
                System.out.println(currentLine);
                jsonBuilder.append(currentLine).append("\n");
            }
            json = jsonBuilder.toString();
            returnJSON = new JSONObject(json);
        } catch (IOException e) {
            System.out.println("Couldn't read from bufferedReader.");
        } catch (JSONException e){
            System.out.println("Error constructing JSON object");
        }

        return returnJSON;
    }
}
