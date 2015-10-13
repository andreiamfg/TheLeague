package com.vfs.theleague.database;

import android.database.sqlite.SQLiteDatabase;

public class ChampionIdTable {

    // Database table
    public static final String TABLE_CHAMP = "champions";
    public static final String KEY_ID = "championId";
    public static final String KEY_NAME = "championName";
    public static final String KEY_IMG = "championImg";

    // The columns on each row
    public static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_IMG};

    // Database creation SQL statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE "+TABLE_CHAMP+" ( " +
                    KEY_ID + " INTEGER, " +
                    KEY_NAME + " TEXT, " +
                    KEY_IMG + " TEXT );";

    public static void onCreate(SQLiteDatabase database)
    {
        // Create new data base with the SQL statement
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        // If database already exists replace it with a new one
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMP);
        onCreate(database);
    }
}
