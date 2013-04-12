package com.example.DirectionParser.Service.RequestFactory;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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

    private static final int STATUS_CODE_OK = 200;

    public RequestFactory(){
        requestParameters = new HashMap<String, String[]>();
        requestParameters.put("mode", null);
        requestParameters.put("waypoints", null);
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

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(new HttpGet(url));
        } catch (IOException e) {
            System.out.println("Couldn't execute url and successfully get a response.");
        }

        StatusLine status = httpResponse.getStatusLine();

        if(status.getStatusCode() != STATUS_CODE_OK)
            System.out.println("Status code not ok : STATUS CODE " + status.getStatusCode());

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Document builder failed to be built.");
        }

        // TODO: finish building document, possibly switch to JAXB for building the XML, then XPATH to navigate.
        return null;

    }

    public String extractParameter(String url, String parameter, String[] parameterValues){

        if(parameter.equals("way-points") && parameterValues.length > 1){

            url = url.concat(parameter + "=" + parameterValues[0] + "|");

            for(int i = 1; i < parameterValues.length - 1; i++){
                url = url.concat(parameterValues[i] + "|");
            }

            url = url.concat(parameterValues[parameterValues.length-1] + "&");
            return url;
        }

        url = url.concat(parameter + "=" + parameterValues[0] + "&");
        return url;
    }
}
