package pt.ipp.estsp.oncohealth.sync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Responsible for connecting to the http server and retrieve the JSON with information.
 * After use, should call {@link ServerConnection#disconnect()}.
 * @author Victor
 * @version 0.1
 * @see HttpURLConnection
 * @since 0.1
 */
public class ServerConnection {
    private HttpURLConnection urlConnection;

    /**
     * Connects to the server. If connection fails throws {@code IOException}.
     * @throws IOException
     */
    public ServerConnection() throws IOException{
        URL url;
        try {
            url = new URL("http://victorfccerqueira.dx.am/document.json");
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the json string from the server and returns it in the {@link JSONObject} format.
     * If connection cannot be read, throws {@code IOException}.
     * @return JSONObject with the json retrieved from the server. If invalid json,
     * return null.
     * @throws IOException
     */
    public JSONObject getJSON() throws IOException{
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

        try {
            return new JSONObject(new String(total.toString().getBytes("ISO-8859-1")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disconnect to the server. Must be called once the json is retrieved;
     */
    public void disconnect(){
        urlConnection.disconnect();
    }
}
