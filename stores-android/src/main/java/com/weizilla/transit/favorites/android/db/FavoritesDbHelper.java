package com.weizilla.transit.favorites.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.weizilla.transit.utils.ResourceUtils;

import java.io.IOException;

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
            db.execSQL(ResourceUtils.readFile("favorites/create_routes_table.sql"));
            db.execSQL(ResourceUtils.readFile("favorites/create_stops_table.sql"));
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
}
