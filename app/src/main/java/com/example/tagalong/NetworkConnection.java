package com.example.tagalong;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class NetworkConnection {

    private static String baseUrl = "http://192.168.148.81:8090";
    private static URL url;

    public boolean checkConnection()
    {
        boolean status = false;

        if(url == null)
        {
            try {
                url = new URL(baseUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                status = true;

                //Toast.makeText(contextForNetwork, "Server says hey", Toast.LENGTH_SHORT).show();
                Log.d("Network", "Connection Success");
            }catch (SocketTimeoutException stoe)
            {
                //Toast.makeText(contextForNetwork, "Server couldn't be reached", Toast.LENGTH_SHORT).show();
                Log.e("Network", stoe.toString());
            }
            catch (IOException e) {
                //Toast.makeText(contextForNetwork, "Failed to create a connection", Toast.LENGTH_SHORT).show();
                Log.e("Network", e.toString());
            }
        }else{
            try {
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                status = true;

                //Toast.makeText(contextForNetwork, "Server says hey", Toast.LENGTH_SHORT).show();
                Log.d("Network", "Connection Success");
            }catch (SocketTimeoutException stoe)
            {
                //Toast.makeText(contextForNetwork, "Server couldn't be reached", Toast.LENGTH_SHORT).show();
                Log.e("Network", stoe.toString());
            }
            catch (IOException e) {
                //Toast.makeText(contextForNetwork, "Failed to create a connection", Toast.LENGTH_SHORT).show();
                Log.e("Network", e.toString());
            }
        }

        return status;
    }

    public boolean disconnect() {
        boolean status = false;

        if (url == null) {
            Log.i("Network", "No url connection to disconnect from");
        } else {
            url = null;
            Log.i("Network", "Url connection terminated successfully");
        }

        return status;
    }

    private String readStream(InputStream bis)
    {

        String data = "";
        byte dataPoint;

        try {

            while ((dataPoint = (byte)bis.read()) != -1)
            {
                data += (char)dataPoint;
            }
        }catch (IOException ex)
        {
            Log.e("Network", "Failed to read stream");
        }

        return data;

    }

    public JSONObject getData(JSONObject data, String endpoint)
    {

        JSONObject result = null;

        /*
        1. Check if a connection to the server can be established
        2. Post data to the server
        3. Wait for response
        */

        try {

            if(baseUrl != null) {

                String parameters ="?";

                if(data != null) {

                    for (int i = 0; i < data.names().length(); i++) {

                        JSONArray keys = data.names();

                        if (i == data.names().length() - 1) {
                            parameters += keys.getString(i) + "=" + data.getString(keys.getString(i));
                        } else {
                            parameters += keys.getString(i) + "=" + data.getString(keys.getString(i)) + "&";
                        }
                    }
                }

                URL url = new URL(baseUrl + "/" + endpoint + parameters);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setChunkedStreamingMode(0);

                OutputStream outs = httpURLConnection.getOutputStream();

                outs.write("".getBytes());

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                String dataForUsage = readStream(inputStream);

                Log.i("Network backfire", dataForUsage);
                result = new JSONObject(dataForUsage);

                httpURLConnection.disconnect();


            }else{
                //Toast.makeText(contextForNetwork, "Base url missing", Toast.LENGTH_SHORT).show();
                Log.e("Network", "Base URL Missing");
            }

        }catch (JSONException jsex)
        {
            //Toast.makeText(contextForNetwork, "There was an error obtaining the response", Toast.LENGTH_SHORT).show();
            Log.e("Network", jsex.toString());
        }
        catch (IOException e) {
            //Toast.makeText(contextForNetwork, "There was an error posting data to the server", Toast.LENGTH_SHORT).show();
            Log.e("Network", e.toString());
        }

        return result;

    }

    public JSONObject postData(JSONObject data, String endpoint)
    {

        JSONObject result = null;

        /*
        1. Check if a connection to the server can be established
        2. Post data to the server
        3. Wait for response
        */

        try {

            if(baseUrl != null) {

                URL url = new URL(baseUrl + "/" + endpoint);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setChunkedStreamingMode(0);

                OutputStream outs = httpURLConnection.getOutputStream();

                outs.write(data.toString().getBytes());

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                result = new JSONObject(readStream(inputStream));

                httpURLConnection.disconnect();

                Log.e("Network", result.toString());


            }else{
                Log.e("Network", "Base Url Missing");
                //Toast.makeText(contextForNetwork, "Base url missing", Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException jsex)
        {
            Log.e("Network", jsex.toString());
            //Toast.makeText(contextForNetwork, "There was an error obtaining the response", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("Network", e.toString());
            //Toast.makeText(contextForNetwork, "There was an error posting data to the server", Toast.LENGTH_SHORT).show();
        }

        return result;

    }


    public URL getUrl(){
        return url;
    }

}
