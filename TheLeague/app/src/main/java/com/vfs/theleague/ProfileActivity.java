package com.vfs.theleague;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vfs.theleague.model.GameInfo;
import com.vfs.theleague.tasks.GetGamesTask;
import com.vfs.theleague.tasks.GetImagesTask;

import java.util.ArrayList;


// Profile activity displays the user profile
// and has a button which leads to another activity
// where a list of games is displayed
public class ProfileActivity extends Activity
{
    SharedPreferences sharedPreferences;       // Reference to store values in shared prefs
    View includedView;                         // Layout that is included in main layout
    public ProgressBar loadingSpinner;         // Progress bar that displays while fetching data

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Initialize spinner and hide it
        loadingSpinner = (ProgressBar)findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);

        // Initialize reference to app context shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        // Get reference to included view to be used to dynamically set data
        includedView = (View) findViewById(R.id.activity_profile_info);

        // Initialize UI Elements
        setProfileImage();
        setLabels();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Hides the progress bar
        setSpinnerOff();
    }

    // onClick function for this activities button
    public void onMatchHistoryCLick(View view)
    {
        // Creates an async task to request the list of recent games for this user
        // Send the activity context for callback
        GetGamesTask getGameHistory = new GetGamesTask(this);
        getGameHistory.execute(APIConstants.getUrlForRecentGames(sharedPreferences.getString("summonerID", "")));
        // Display spinner while async task executes
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    // Callback for GetGamesTask, starts the list activity
    // and sends the arraylist of objects for the adapter
    public void startGamesActivity(ArrayList<GameInfo> gameInfoList)
    {
        Intent thisIntent = new Intent(this, GamesListActivity.class);
        thisIntent.putParcelableArrayListExtra("gameList", gameInfoList);
        startActivity(thisIntent);
    }


    // Sets all dynamic text fields in this activity
    public void setLabels()
    {
        TextView thisSummonerName = (TextView) includedView.findViewById(R.id.profile_summoner_name);
        TextView thisSummonerLevel = (TextView) includedView.findViewById(R.id.profile_summoner_level);
        TextView thisSummonerKills = (TextView) includedView.findViewById(R.id.profile_kills_counter);
        TextView thisSummonerAssists = (TextView) includedView.findViewById(R.id.profile_assists_counter);
        TextView thisSummonerWins = (TextView) includedView.findViewById(R.id.profile_wins_counter);

        thisSummonerName.setText(sharedPreferences.getString("summonerName", ""));
        thisSummonerLevel.setText(sharedPreferences.getString("summonerLevel", ""));
        thisSummonerKills.setText(sharedPreferences.getString("summonerKills", ""));
        thisSummonerAssists.setText(sharedPreferences.getString("summonerAssists", ""));
        thisSummonerWins.setText(sharedPreferences.getString("summonerWins", ""));

    }

    // Sets user icon
    public void setProfileImage()
    {
        ImageView summonerImageView = (ImageView) includedView.findViewById(R.id.profile_summoner_icon);

        String iconId = sharedPreferences.getString("summonerIcon", "");
        // Try loading image from cache
        final Bitmap bitmap = (Bitmap)Cache.getInstance().getLruCache().get(iconId);

        // If it is successful, use it
        // else request it through async request
        if (bitmap != null)
        {
            // This means image was in cache
            summonerImageView.setImageBitmap(bitmap);
        }
        else
        {
            // This means image was not in cache
            new GetImagesTask(summonerImageView,iconId).execute(APIConstants.getUrlForSummIcon()+iconId+".png");
        }
    }

    // Hides progress bar
    void setSpinnerOff()
    {
        loadingSpinner.setVisibility(View.GONE);
    }

    // Callback for when no recent games were retrieved
    public void noDataError()
    {
        Toast.makeText(this, "Recent games not found", Toast.LENGTH_SHORT).show();
        setSpinnerOff();
    }

    // Callback for when no connection was available
    public void internetError()
    {
        Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        setSpinnerOff();
    }

    // Returns true if either Wifi or Data are on
    public boolean checkIfNetworkStateOn()
    {
        // Get Network State from Connectivity Manager
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // Returns true if there is an active Network and its available
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
