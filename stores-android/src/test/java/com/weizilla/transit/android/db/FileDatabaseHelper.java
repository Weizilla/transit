package com.weizilla.transit.android.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Collections;

public class FileDatabaseHelper extends DatabaseHelper
{
    private File file;

    public FileDatabaseHelper(File file)
    {
        super(null, Collections.<String>emptyList());
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
