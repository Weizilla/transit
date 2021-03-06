package com.weizilla.transit.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.weizilla.transit.utils.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    protected static final String DB_NAME = "Favorites.db";
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int VERSION = 1;
    private final List<String> creationFiles = new ArrayList<>();

    public DatabaseHelper(Context context, List<String> creationFiles)
    {
        super(context, DB_NAME, null, VERSION);
        this.creationFiles.addAll(creationFiles);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            for (String file : creationFiles)
            {
                db.execSQL(ResourceUtils.readFile(file));
            }
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
