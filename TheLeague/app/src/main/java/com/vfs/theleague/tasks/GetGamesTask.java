package com.vfs.theleague.tasks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.vfs.theleague.ProfileActivity;
import com.vfs.theleague.model.GameInfo;
import com.vfs.theleague.JSONParser;
import com.vfs.theleague.MainActivity;
import com.vfs.theleague.TheLeagueWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// This class retrieves a list of recent matches for a summonerID
public class GetGamesTask extends AsyncTask<String, Void, ArrayList<GameInfo>>
{
    private ProfileActivity profileActivity;    // Reference for callback function
    private boolean hasConnection = false;

    public GetGamesTask(ProfileActivity theProfileActivity)
    {
        profileActivity = theProfileActivity;
    }

    // This gets called by execute
    @Override
    protected ArrayList<GameInfo> doInBackground(String... params)
    {
        checkConnection(); // Returns true if a connection is available

        ArrayList<GameInfo> listOfGames = new ArrayList<GameInfo>();

        if(hasConnection)
        {
            for (String url: params)
            {
                // If there is a connection available
                fillGamesList(url, listOfGames);

            }
        }
        // Sends it to execute
        return listOfGames;
    }

    void fillGamesList(String url, ArrayList<GameInfo> listOfGames)
    {
        JSONObject myJsonGameListObj = JSONParser.getJSONObjectFromURL(url);
        if(myJsonGameListObj!=null)
        {
            JSONArray allGames = null;
            try
            {
                allGames = myJsonGameListObj.getJSONArray("games");
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }

            for (int i = 0; i < allGames.length(); i++)
            {
                try
                {
                    JSONObject game = allGames.getJSONObject(i);

                    String mode = game.getString("gameMode");
                    String champion = game.getString("championId");
                    String gameOutcome = game.getJSONObject("stats").getString("win").toString();

                    // Convert boolean status to Victory/Defeat
                    if (gameOutcome == "true")
                    {
                        gameOutcome = "Victory";
                    }
                    else if (gameOutcome == "false")
                    {
                        gameOutcome = "Defeat";
                    }

                    // Create a new object with information retrieved
                    GameInfo newGame = new GameInfo(mode, champion, gameOutcome);
                    // Add it to the list of Games
                    listOfGames.add(newGame);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    // Checks if its possible to establish network connection
    void checkConnection()
    {
        if (profileActivity.checkIfNetworkStateOn())
        {
            try
            {
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

    // SENDS BACK TO MAIN THREAD ACTIVITY by calling its function
    @Override
    protected void onPostExecute(ArrayList<GameInfo> allTheGames)
    {
        if(hasConnection)
        {
            if (!allTheGames.isEmpty())
            {
                // Returns list for the next activity
                profileActivity.startGamesActivity(allTheGames);
            }
            else
            {
                profileActivity.noDataError();
            }
        }
        else
        {
            profileActivity.internetError();
        }
    }
}
