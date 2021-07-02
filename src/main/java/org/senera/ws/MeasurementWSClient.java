package org.senera.ws;

import android.util.Log;

import org.json.JSONObject;
import org.senera.server.ws.Measurement;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 26/4/2017.
 */

public class MeasurementWSClient {

    // String for LogCat documentation
    private final static String TAG = "SENERA-MeasurementWS";

    private static URL url;

    public MeasurementWSClient(String serverHostname, int serverPort) {
        try {
            url = new URL("http://" + serverHostname + ":" + serverPort + "/SeneraServer/rest/measurement");
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveMeasurement(Measurement measurement){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());

            JSONObject json = measurement.toJson();
            osw.write(json.toString());
            osw.flush();
            osw.close();

            int response = connection.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
