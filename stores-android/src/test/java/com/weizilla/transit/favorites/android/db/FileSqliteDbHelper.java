package com.weizilla.transit.favorites.android.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class FileSqliteDbHelper extends FavoritesDbHelper
{
    private File file;

    public FileSqliteDbHelper(File file)
    {
        super(null);
        this.file = file;
    }

    @Override
    public SQLiteDatabase getWritableDatabase()
    {
        return SQLiteDatabase.openOrCreateDatabase(file, null);
    }

    @Override
    public SQLiteDatabase getReadableDatabase()
    {
        return SQLiteDatabase.openOrCreateDatabase(file, null);
    }
}
