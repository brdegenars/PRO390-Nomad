package com.example.DirectionParser.Service.RequestFactory;

import org.apache.http.HttpRequest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private Map<String, String[]> requestParameters;
    private String mode;

    public RequestFactory(){
        requestParameters = new HashMap<String, String[]>();
        requestParameters.put("mode", null);
        requestParameters.put("way-points", null);
        requestParameters.put("avoid", null);
        requestParameters.put("units", null);
    }

    public HttpRequest getRequest(){

        String url = "https://maps.googleapis.com/maps/api/directions/";

        for(String parameter : requestParameters.keySet()){

            String[] currentValue = requestParameters.get(parameter);
            if(currentValue != null){
                // TODO: check from mode to units and concat onto the request the appropriate optional parameters.
                extractParameter(url, parameter, currentValue);
            }
        }

        URLConnection mapRequestURL = null;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try{

            URL urlObject = new URL("https://");
            mapRequestURL = urlObject.openConnection();
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

    public String extractParameter(String url, String parameter, String[] parameterValue){

        if(parameter.equals("way-points")){
           // TODO: format url for multiple waypoints.
        }
        url.concat(parameter + "=" + parameterValue + "&");
        return url;
    }
}
