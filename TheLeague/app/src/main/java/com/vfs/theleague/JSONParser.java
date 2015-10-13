package com.vfs.theleague;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jsonArray = null;
    static String json = "";

    public static JSONObject getJSONObjectFromURL(String url)
    {
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        }
        catch (Exception e)
        {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        }
        catch (Exception e)
        {
            Log.e("log_tag get data","Error converting result " + e.toString());
        }

            try
            {
                jObject = new JSONObject(result);
            }
            catch (JSONException e)
            {
                Log.e("log_tag create object ", "Error parsing data " + e.toString());
            }

        return jObject;
    }

}
