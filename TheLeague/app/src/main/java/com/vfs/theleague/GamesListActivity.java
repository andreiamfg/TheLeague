package com.vfs.theleague;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.vfs.theleague.model.GameInfo;

import java.util.ArrayList;

// Populated by list items filled with different games information
public class GamesListActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        final ListView listView = (ListView) findViewById(R.id.list);
        // On create set list view adapter
        // with information retrieved in the intent
        ArrayList<GameInfo> gamesListArray = getIntent().getParcelableArrayListExtra("gameList");
        GamesListAdapter gamesListAdapter = new GamesListAdapter(this, gamesListArray);
        listView.setAdapter(gamesListAdapter);
    }
}
