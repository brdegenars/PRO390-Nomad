package com.example.DirectionParser.Service.RequestFactory;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/11/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestFactory {

    private Map<String, String> requestParameters;
    private String mode;

    public RequestFactory(){
        requestParameters = new HashMap<String, String>();
        requestParameters.put("mode", null);
        requestParameters.put("waypoints", null);
        requestParameters.put("avoid", null);
        requestParameters.put("units", null);
    }

    public HttpRequest getRequest(){

        for(String parameter : requestParameters.values()){

            if(!parameter.isEmpty()){
                // TODO: check from mode to units and concat onto the request the appropriate optional parameters.

            }
        }

        URLConnection mapRequestURL = null;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try{

            URL url = new URL("https://");
            mapRequestURL = url.openConnection();
            mapRequestURL.connect();

            outputStream = mapRequestURL.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);

            // TODO: Either just JAXB or XPATH or something to parse the XML from the buffered stream above.

        } catch (MalformedURLException e){

            e.printStackTrace();

        } catch (IOException e){

            e.printStackTrace();

        }

        return null;

    }
}
