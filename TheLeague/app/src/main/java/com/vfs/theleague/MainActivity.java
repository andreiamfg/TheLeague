package com.vfs.theleague;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vfs.theleague.database.GetDataBase;
import com.vfs.theleague.database.TheLeagueSQLiteHelper;
import com.vfs.theleague.model.Champion;
import com.vfs.theleague.model.SummonerInfo;
import com.vfs.theleague.tasks.GetProfileTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class MainActivity extends Activity
{
    private static MainActivity MAIN_ACTIVITY;   // Reference to activity to be used by Widget
    public ProgressBar loadingSpinner;          // System progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Assign this activity to global variable
        MAIN_ACTIVITY = this;

        // Set progress bar
        loadingSpinner = (ProgressBar)findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);

        // Access to sharedPrefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        // If its the first time app is running create a SQLite database
        // that maps champion ids to their names, to be used for requesting images
        if(!sharedPreferences.getBoolean("firstTime", false))
        {
            createDatabase.start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // Store in shared preferences for future launches
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Hide progress bar
        setSpinnerOff();
    }

    public void onSearch(View view)
    {
        // Set edit text field
        EditText summonerInputField = (EditText) findViewById(R.id.summoner_input_field);
        // Get Summoner Name
        String summonerName =summonerInputField.getText().toString();
        // Convert to lower case and clear spaces
        summonerName = fixString(summonerName);

        // If the field is not empty
        if (!summonerName.matches(""))
        {
            // Start an async task that retrieves a user id based on the name
            // and then generates another call to retrieve the users's info
            GetProfileTask getSummonerInfo = new GetProfileTask(this, summonerName);
            // Use Api Constants to generate correct urls
            getSummonerInfo.execute(APIConstants.getUrlForSummoner(summonerName));
            loadingSpinner.setVisibility(View.VISIBLE);
        }
        else
        {
            // Throw notification
            Toast.makeText(this, "The field is empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Fixes the name string to be used in url path
    private String fixString(String input)
    {
        // Deletes all spaces
        input = input.replaceAll("\\s", "");
        // Converts all chars to lowercase
        input = input.toLowerCase();
        return input;
    }

    // Get Main activity context, used by widget
    public static MainActivity getActivity()
    {
        return MAIN_ACTIVITY;
    }

    // Callback from GetSummonerTask request
    // When user profile info is retrieved automatically starts next activity
    public void startProfileActivity(SummonerInfo summonerInfo)
    {
        Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileActivityIntent);
        // No information is sent because it was stored in shared preferences
        // to be used by both ProfileActivity and TheLeagueWidget
    }

    // Toast notifying that no user with that name was found
    public void summonerNotFound()
    {
        Toast.makeText(this, "Summoner doesn't exist", Toast.LENGTH_SHORT).show();
        setSpinnerOff();
    }

    void setSpinnerOff()
    {
        loadingSpinner.setVisibility(View.GONE);
    }

    public void internetError()
    {
        Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        setSpinnerOff();
    }

    public boolean internetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.e("Connection", String.valueOf(isConnected));
        return isConnected;
    }

    // Runs in the background the first time app is launched
    // Creates a database with 3 columns: champion id, name, and imageName
    // which will be used to request images from the API
    Thread createDatabase = new Thread()
    {
        @Override
        public void run()
        {
            // Request Json object from url
            JSONObject championJsonObj = JSONParser.getJSONObjectFromURL(APIConstants.getUrlForChampions());
            // Get SQLite database
            TheLeagueSQLiteHelper db = new TheLeagueSQLiteHelper(MainActivity.getActivity());

            //If Json object is not null start adding stuff to the database
            if(championJsonObj!=null)
            {
                JSONObject allChampions = null; //Create JSon object to hold one that is inside the general Json

                try
                {
                    allChampions = championJsonObj.getJSONObject("data"); // Get the Json data Json object from the API
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                // Loop trough all the keys from the Json object
                for(Iterator iterator = allChampions.keys(); iterator.hasNext();)
                {
                    String key = (String) iterator.next(); // Get key retrived by the iterator
                    try
                    {
                        String id = allChampions.getJSONObject(key).get("key").toString(); // Store "key" value in temporary var
                        String name = allChampions.getJSONObject(key).get("name").toString(); // Store "name" value in temporary var
                        String img = allChampions.getJSONObject(key).getJSONObject("image").get("full").toString(); // Store "imageName" value in temporary var

                        db.addChampion(new Champion(Integer.parseInt(id),name,img)); // Add this values to the SQlite database
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
    };
}
