package com.vfs.theleague;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vfs.theleague.database.GetDataBase;
import com.vfs.theleague.database.TheLeagueSQLiteHelper;
import com.vfs.theleague.model.GameInfo;
import com.vfs.theleague.tasks.GetImagesTask;

import java.util.ArrayList;

// Adapter for the list view that displays recent games
public class GamesListAdapter extends ArrayAdapter<GameInfo>
{
    // Access to the database where id and name for images are stored
    TheLeagueSQLiteHelper champKeysDatabase = new TheLeagueSQLiteHelper(MainActivity.getActivity());

    Integer champId;    // Key for database
    String imageName;   // Name.png to add in path for image requests

    public GamesListAdapter(Context context, ArrayList<GameInfo> allGames)
    {
        super(context,R.layout.list_games, allGames);
    }

    // This is called for each item in list
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the object in this position in arraylist
        GameInfo currentGame = this.getItem(position);

        // Get list item layout
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_games, parent, false);
        }

        // Turn string retrieved from object to int to use as key for database
        champId = Integer.parseInt(currentGame.getChampion());

        // Set Labels with
        // Game Mode
        TextView gameModeTextView = (TextView) convertView.findViewById(R.id.textView_game_mode);
        gameModeTextView.setText(currentGame.getGameMode().toLowerCase());
        // Champion Name
        TextView championNameTextView = (TextView) convertView.findViewById(R.id.textView_champion_name);
        championNameTextView.setText(champKeysDatabase.getChampion(champId).getName());
        // Outcome: Victory/Defeat
        TextView winLoseTextView = (TextView) convertView.findViewById(R.id.textView_win_lose);
        winLoseTextView.setText(currentGame.getOutcome().toUpperCase());

        // Set reference to the image view
        ImageView championImageView = (ImageView) convertView.findViewById(R.id.imageView_champ);
        // Get the image name to be requested
        imageName = champKeysDatabase.getChampion(champId).getImg();

        // Check if its already in cache
        final Bitmap bitmap = (Bitmap)Cache.getInstance().getLruCache().get(currentGame.getChampion());

        if (bitmap != null)
        {
            // If it was in cache, set it
            championImageView.setImageBitmap(bitmap);
        }
        else
        {
            // Otherwise request from network
            new GetImagesTask(championImageView,currentGame.getChampion()).execute(APIConstants.getUrlForChampIMG() + imageName);
        }

        // return view converted to row
        return convertView;
    }

}
