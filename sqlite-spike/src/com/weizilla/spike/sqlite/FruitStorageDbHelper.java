package com.weizilla.spike.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * db helper for storing fruit
 *
 * @author wei
 *         Date: 11/16/13
 *         Time: 2:25 PM
 */
public class FruitStorageDbHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "FruitStorage.db";
    private static final String CREATE_SQL =
        "CREATE TABLE " + FruitContract.FruitEntry.TABLE_NAME + " (" +
        FruitContract.FruitEntry._ID + " INTEGER PRIMARY KEY," +
        FruitContract.FruitEntry.NAME + " TEXT," +
        FruitContract.FruitEntry.SIZE + " TEXT" +
        " )";
    private static final String DROP_TABLE_SQL =
        "DROP TABLE IF EXISTS " + FruitContract.FruitEntry.TABLE_NAME;

    public FruitStorageDbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_TABLE_SQL);
        onCreate(db);
    }
}
