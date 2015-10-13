package com.vfs.theleague;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.vfs.theleague.tasks.GetWidgetImageTask;

// TheLeagueWidget updates the UI of the widget every 30 min
// Or when the user changes values inside the app

public class TheLeagueWidget extends AppWidgetProvider
{
    SharedPreferences sharedPreferences; // Reference to the shared preferences data
    public RemoteViews views;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getActivity().getApplicationContext());

        // Get the views from the widget
        views = new RemoteViews(context.getPackageName(), R.layout.the_league_widget);

        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; i++)
        {
            try
            {
                // Create an intent to the app main activity
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                // Get iconId to use in a future request
                String iconId = sharedPreferences.getString("summonerIcon", "");

                try
                {
                    // Set the values of the widget's views
                    setValues();
                    // Make an async request for the widget icon
                    new GetWidgetImageTask().execute(APIConstants.getUrlForSummIcon() + iconId + ".png");

                }
                catch (ActivityNotFoundException e)
                {

                }

                // Open app when clicked on summoner icon
                views.setOnClickPendingIntent(R.id.profile_summoner_icon, pendingIntent);

                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
            catch (ActivityNotFoundException e)
            {
                Toast.makeText(context.getApplicationContext(), "There was a problem loading the application: ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setValues()
    {
        // Set all the values for the views
        views.setTextViewText(R.id.profile_summoner_name, sharedPreferences.getString("summonerName", ""));
        views.setTextViewText(R.id.profile_summoner_level, sharedPreferences.getString("summonerLevel", ""));
        views.setTextViewText(R.id.profile_kills_counter, sharedPreferences.getString("summonerKills", ""));
        views.setTextViewText(R.id.profile_assists_counter, sharedPreferences.getString("summonerAssists", ""));
        views.setTextViewText(R.id.profile_wins_counter, sharedPreferences.getString("summonerWins", ""));

        byte[] decodedByte = Base64.decode(sharedPreferences.getString("bitmapEncoded", ""), 0);
        // Decode string into bitmap to show the icon
        Bitmap bm = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

        views.setImageViewBitmap(R.id.profile_summoner_icon, bm);
    }

}


