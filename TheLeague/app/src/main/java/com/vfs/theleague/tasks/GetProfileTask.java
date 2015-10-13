package com.vfs.theleague.tasks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vfs.theleague.APIConstants;
import com.vfs.theleague.JSONParser;
import com.vfs.theleague.MainActivity;
import com.vfs.theleague.TheLeagueWidget;
import com.vfs.theleague.model.SummonerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// Given a summoner name, this class sends a request for a summoner ID
// After retrieving it, it immediately sends another request for the users info
public class GetProfileTask extends AsyncTask<String, Void, SummonerInfo>
{
    private MainActivity mainActivity;      // Reference to main activity for accessing callback
    private boolean hasConnection = false;  // Boolean used to check Connectivity status

    private String summonerName;            // Fixed User Name
    private String summonerId = null;       // User id for requesting user info

    public GetProfileTask(MainActivity theMainActivity, String name)
    {
        this.mainActivity = theMainActivity;
        this.summonerName = name;
    }

    @Override
    protected SummonerInfo doInBackground(String... params)
    {
        // Initialize summoner object which will store info retrieved from json
        SummonerInfo thisSummoner = new SummonerInfo();
        checkConnection(); // If there is a network available check for connection
        // If there is a connection, send request to Network
        if(hasConnection)
        {
            for (String url: params)
            {
                fillSummonerJson(url, thisSummoner); // Populates object with info retrieved
            }
        }
        return thisSummoner;
    }

    void fillSummonerJson(String url, SummonerInfo thisSummoner)
    {
        JSONObject myJsonSummonerObj = JSONParser.getJSONObjectFromURL(url);

        // If Some information was retrieved
        if(myJsonSummonerObj!=null)
        {
            try {
                // Try to extract info
                JSONObject summonerArray = myJsonSummonerObj.getJSONObject(summonerName);
                summonerId = summonerArray.getString("id");
                thisSummoner.setSummonerId(summonerId);
                thisSummoner.setSummonerName(summonerArray.getString("name"));
                thisSummoner.setSummonerIcon(summonerArray.getString("profileIconId"));
                thisSummoner.setSummonerLevel(summonerArray.getString("summonerLevel"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            JSONObject myJsonSummonerStats = JSONParser.getJSONObjectFromURL(APIConstants.getUrlForSummonerStats(summonerId));
            JSONArray allStats = null;
            try
            {
                allStats = myJsonSummonerStats.getJSONArray("playerStatSummaries");
            } catch (JSONException e)
            {
                e.printStackTrace();

            }

            // Create int to iterate through array and find the right game type
            // Since array has different sizes and positions for different players
            int statsPosInArray = 0;
            if (allStats != null)
            {
                try
                {
                    // For each position in array check if key is the one we are looking for
                    for (statsPosInArray = 0; statsPosInArray < allStats.length(); statsPosInArray++)
                    {
                        if (allStats.getJSONObject(statsPosInArray).getString("playerStatSummaryType") == "Unranked")
                        {
                            // When we find it we break from loop
                            break;
                        }
                    }
                    // Array is incrementing the last time, so we must decrease by one
                    statsPosInArray -= 1;

                    //Use this position to access correct array and get the value for wins
                    thisSummoner.setSummonerWins(allStats.getJSONObject(statsPosInArray).getString("wins"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                JSONObject summonerStats = null;
                try
                {
                    summonerStats = allStats.getJSONObject(statsPosInArray).getJSONObject("aggregatedStats");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (summonerStats != null)
                {
                    try
                    {
                        thisSummoner.setSummonerKills(summonerStats.getString("totalChampionKills"));
                        thisSummoner.setSummonerAssists(summonerStats.getString("totalAssists"));
                    }
                    catch (JSONException e)
                    {

                        e.printStackTrace();
                    }
                }
            }
        }

    }

    // Checks if application is able to connect to network
    void checkConnection()
    {
        // If wireless state is on
        if (mainActivity.internetConnection())
        {
            try
            {
                // Try to create connection
                HttpURLConnection urlc = (HttpURLConnection)(new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                hasConnection = (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            }
            catch (IOException e)
            {
                Log.e("Error", "Error checking internet connection", e);
                hasConnection = false;
            }
        }
    }

    @Override
    protected void onPostExecute(SummonerInfo thisSummoner)
    {
        // If there's connection then the summoner object is filled
        if(hasConnection)
        {
            // Check on of the contents to assert that it is correct information
            if(thisSummoner.getSummonerName()!= null)
            {
                SharedPreferences sharedDataBase = PreferenceManager.getDefaultSharedPreferences(mainActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedDataBase.edit();

                // Save summoner Info information to shared Prefs so it can be used by widget
                editor.putString("summonerName", thisSummoner.getSummonerName());
                editor.putString("summonerID", thisSummoner.getSummonerId());
                editor.putString("summonerIcon", thisSummoner.getSummonerIcon());
                editor.putString("summonerLevel", thisSummoner.getSummonerLevel());
                editor.putString("summonerKills", thisSummoner.getSummonerKills());
                editor.putString("summonerAssists", thisSummoner.getSummonerAssists());
                editor.putString("summonerWins",thisSummoner.getSummonerWins());
                editor.putBoolean("widgetFirst", true);

                editor.apply();

                // SEnd an intent to update the widget
                Intent intent = new Intent(MainActivity.getActivity(),TheLeagueWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(mainActivity.getApplication()).getAppWidgetIds(new ComponentName(mainActivity.getApplication(), TheLeagueWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                mainActivity.sendBroadcast(intent);

                // Execute callback in main activity with the info retrieved
                mainActivity.startProfileActivity(thisSummoner);
            }
            else
            {
                // Else if the summoner Info was not valid call an error toast
                mainActivity.summonerNotFound();
            }
        }
        else
        {
            // If there was no connection show notification
            mainActivity.internetError();
        }

    }
}
