package com.weizilla.transit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collections;
import java.util.List;

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
    private List<String> createSqls;
    private List<String> dropSqls;

    public SqliteDbHelper(Context context, String dbName,
            String createSql, String dropSql)
    {
        this(context, dbName, Collections.singletonList(createSql),
                Collections.singletonList(dropSql));
    }

    public SqliteDbHelper(Context context, String dbName,
            List<String> createSqls, List<String> dropSqls)
    {
        super(context, dbName, null, VERSION);
        this.createSqls = createSqls;
        this.dropSqls = dropSqls;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        for (String createSql : createSqls)
        {
            db.execSQL(createSql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (String dropSql : dropSqls)
        {
            db.execSQL(dropSql);
        }
        onCreate(db);
    }
}
