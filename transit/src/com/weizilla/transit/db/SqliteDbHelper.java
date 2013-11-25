package com.weizilla.transit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Generic class for database helper
 *
 * @author wei
 *         Date: 11/24/13
 *         Time: 6:27 PM
 */
public class SqliteDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private String createSql;
    private String dropSql;

    public SqliteDbHelper(Context context,
            String dbName, String createSql, String dropSql)
    {
        super(context, dbName, null, VERSION);
        this.createSql = createSql;
        this.dropSql = dropSql;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(dropSql);
        db.execSQL(createSql);
    }
}
