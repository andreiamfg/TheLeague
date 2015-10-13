package com.vfs.theleague.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vfs.theleague.model.Champion;

public class TheLeagueSQLiteHelper extends SQLiteOpenHelper
{
    // Give a name and version to the data base
    private static final String DATABASE_NAME = "champion.db";
    private static final int DATABASE_VERSION = 1;

    public TheLeagueSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        ChampionIdTable.onCreate(database);
    }
    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion)
    {
        ChampionIdTable.onUpgrade(database, oldVersion, newVersion);
    }

    public void addChampion(Champion champ)
    {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ChampionIdTable.KEY_IMG, champ.getImg()); // get Img
        values.put(ChampionIdTable.KEY_NAME, champ.getName()); // get Name
        values.put(ChampionIdTable.KEY_ID, champ.getId()); // get ID
        // insert
        db.insert(ChampionIdTable.TABLE_CHAMP,null,values);
        // close
        db.close();
    }

    public Champion getChampion(int id){

        // get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // build query
        Cursor cursor = db.query(ChampionIdTable.TABLE_CHAMP, // table
                        ChampionIdTable.COLUMNS, // column names
                        " championId = ?", // selection
                        new String[] { String.valueOf(id) }, // arguments
                        null, // group by
                        null, // having
                        null, // order by
                        null); // limit


        // if we got results get the first one
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        // build champion object
        Champion champ = new Champion();

        champ.setId(Integer.parseInt(cursor.getString(0)));
        champ.setName(cursor.getString(1));
        champ.setImg(cursor.getString(2));

        cursor.close();
        // return champion
        return champ;
    }

}
