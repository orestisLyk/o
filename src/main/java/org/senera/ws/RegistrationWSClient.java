package org.senera.ws;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by George on 14/4/2017.
 */

public class RegistrationWSClient {

    // String for LogCat documentation
    private final static String TAG = "SENERA-RegistrationWS";

    private static URL url;

    public RegistrationWSClient(String serverHostname, int serverPort) {
        try {
            url = new URL("http://" + serverHostname + ":" + serverPort + "/SeneraServer/rest/register");
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean register(String id, int port, String sensorType){
        boolean success = true;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());

            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("port", port);
            json.put("sensorType", sensorType);
            osw.write(json.toString());
            osw.flush();
            osw.close();

            int response = connection.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){

            }
        } catch (IOException e) {
            success = false;
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            success = false;
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return success;
    }

}
