package com.example.DirectionParser.Service.RequestFactory;

import com.example.DirectionParser.Service.AsyncTasks.RequestAsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

    protected Map<String, String[]> requestParameters;

    private static final int STATUS_CODE_OK = 200;

    public RequestFactory(){
        requestParameters = new HashMap<String, String[]>();
        requestParameters.put("mode", null);
        requestParameters.put("waypoints", null);
        requestParameters.put("avoid", null);
        requestParameters.put("units", null);
    }

    public HttpResponse makeRequest(ArrayList<String[]> parameterValues){

        String url = "http://maps.googleapis.com/maps/api/directions/xml?";

        url = extractParameter(url, "origin", parameterValues.get(0), false);
        url = extractParameter(url, "destination", parameterValues.get(1), false);
        url = extractParameter(url, "sensor", parameterValues.get(2), false);
        url = extractParameter(url, "mode", parameterValues.get(3), false);
//        url = extractParameter(url, "waypoints", parameterValues.get(4), false);
        url = extractParameter(url, "avoid", parameterValues.get(5), false);
        url = extractParameter(url, "units", parameterValues.get(6), true);

        RequestAsyncTask sendRequest = new RequestAsyncTask();
        sendRequest.execute(url);
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpResponse httpResponse;

/*        StatusLine status = httpResponse.getStatusLine();

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
*/
      // TODO: finish resolving the document received, possibly switch to JAXB for building the XML document instead of DOM, then XPATH to navigate.
        return null;
    }

    public String extractParameter(String url, String parameter, String[] parameterValues, boolean lastParam){

        if(parameter.equals("way-points") && parameterValues.length > 1){

            url = url.concat(parameter + "=" + parameterValues[0] + "|");

            for(int i = 1; i < parameterValues.length - 1; i++)
                url = url.concat(parameterValues[i] + "|");

            url = url.concat(parameterValues[parameterValues.length-1] + "&");
        } else
            url = url.concat(parameter + "=" + parameterValues[0]);

        if(!lastParam) url = url.concat("&");

        return url;
    }
}
