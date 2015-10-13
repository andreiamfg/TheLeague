package com.vfs.theleague.database;

import com.vfs.theleague.MainActivity;

public class GetDataBase
{
    public static TheLeagueSQLiteHelper getDataBase(MainActivity theMainActivity)
    {
        TheLeagueSQLiteHelper db = new TheLeagueSQLiteHelper(theMainActivity);
        return db;
    }

}
