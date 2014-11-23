package com.weizilla.transit.favorites.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class FavoritesDbHelper extends SQLiteOpenHelper
{
    protected static final String DB_NAME = "Favorites.db";
    private static final String TAG = FavoritesDbHelper.class.getSimpleName();
    private static final int VERSION = 1;

    public FavoritesDbHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            String sql = readSqlFromFile("create_routes_table.sql");
            db.execSQL(sql);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error creating favorites db", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        throw new RuntimeException("on upgrade");
    }

    //TODO move into utils and combe w/ other function
    private static String readSqlFromFile(String sqlFile) throws IOException
    {
        URL url = Resources.getResource(sqlFile);
        return Resources.toString(url, Charsets.UTF_8);
    }
}
