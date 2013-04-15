package com.example.DirectionParser.Service.RequestFactory;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/11/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestFactory {

    protected Map<String, String[]> requestParameters;

    private static final int STATUS_CODE_OK = 200;

    public RequestFactory(){
        requestParameters = new HashMap<String, String[]>();
        requestParameters.put("mode", null);
        requestParameters.put("waypoints", null);
        requestParameters.put("avoid", null);
        requestParameters.put("units", null);
    }

    public HttpResponse getRequest(List<String[]> parameterValues){

        String url = "https://maps.googleapis.com/maps/api/directions/";

        int valueIndex = 0;
        for(String parameter : requestParameters.keySet()){

            requestParameters.put(parameter, parameterValues.get(valueIndex++));
            String[] currentValue = requestParameters.get(parameter);

            if(currentValue != null){
                // TODO: check from mode to units and concat onto the request the appropriate optional parameters.
                url = extractParameter(url, parameter, currentValue);
            }
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(new HttpGet(url));
        } catch (IOException e) {
            System.out.println("Couldn't execute url and successfully get a response.");
            return null;
        }

        StatusLine status = httpResponse.getStatusLine();

        if(status.getStatusCode() != STATUS_CODE_OK)
            System.out.println("Status code not ok : STATUS CODE " + status.getStatusCode());

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        Document document;

        try {

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(httpResponse.getEntity().getContent());

        } catch (ParserConfigurationException e) {
            System.out.println("Document builder failed to be built.");
            return null;
        } catch (IOException e){
            System.out.println("Response failed to retrieve content.");
            return null;
        } catch (SAXException e){
            System.out.println("Document builder was unsuccessful in parsing response content.");
            return null;
        }

        System.out.println("Retrieved document of type " + document.getDoctype());
        // TODO: finish resolving the document received, possibly switch to JAXB for building the XML document instead of DOM, then XPATH to navigate.
        return httpResponse;
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
