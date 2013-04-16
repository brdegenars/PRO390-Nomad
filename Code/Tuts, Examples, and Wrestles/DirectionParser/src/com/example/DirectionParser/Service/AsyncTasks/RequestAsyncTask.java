package com.example.DirectionParser.Service.AsyncTasks;

import android.os.AsyncTask;
import android.util.Xml;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.logging.XMLFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/16/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestAsyncTask extends AsyncTask<String, String, Xml>{

    public static final int STATUS_CODE_OK = 200;

    @Override
    protected Xml doInBackground(String[] params) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse httpResponse = null;

        // TODO: It doesn't like line 34 below. Find out why.
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

        return null;
    }
}
