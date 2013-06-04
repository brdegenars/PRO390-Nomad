package service;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/31/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrafficURLGenerator {

    public  static final int NORTH = 0,
                             SOUTH = 1,
                             EAST = 2,
                             WEST = 3;
    public static final String API_KEY_VALUE = "AlrXLl21c3dQrlMLorr4Ob-Y12UOaCclC1YrQ9UtXiVOG1OBz_Bj432uZuXEU8r7";
    private static final String INCIDENT_SEVERITY = "severity";
    private static final String INCIDENT_TYPE = "type";
    private static final String API_KEY = "key";

    private static String url ="http://dev.virtualearth.net/REST/v1/Traffic/Incidents/";

    private String[] cardinalBounds, severity, type;

    private void initalizeGenerator(String[] cardinalBounds, String[] severity, String[] type){
        this.cardinalBounds = new String[cardinalBounds.length];
        this.severity = new String[severity.length];
        this.type = new String[type.length];

        System.arraycopy(cardinalBounds, 0, this.cardinalBounds, 0, cardinalBounds.length);
        System.arraycopy(severity, 0, this.severity, 0, severity.length);
        System.arraycopy(type, 0, this.type, 0, type.length);
    }

    public static String generateURL(String[] cardinalBounds, String[] severity, String[] type) throws ExecutionException, InterruptedException {

        extractParameter(cardinalBounds, TrafficURLGenerator.INCIDENT_SEVERITY);
        extractParameter(severity, TrafficURLGenerator.INCIDENT_TYPE);
        extractParameter(type, TrafficURLGenerator.API_KEY);
        extractParameter(new String[]{TrafficURLGenerator.API_KEY_VALUE}, null);

        return url;
    }

    private static void extractParameter(String[] parameterValues, String nextParameterName){

        for(int i = 0; i <parameterValues.length; i++){
            url = url.concat(String.valueOf(parameterValues[i]));
            if (parameterValues.length > 1 && i < parameterValues.length - 1) url = url.concat(",");
        }
        if(nextParameterName != null){
            if(nextParameterName.equals("severity")) url = url.concat("?");
            url = url.concat("&" + nextParameterName + "=");
        }
    }
}
