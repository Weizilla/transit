package com.weizilla.transit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.weizilla.transit.data.FavRoute;

/**
 * Handles all CRUD operations related to favorite routes
 * TODO switch to content provider
 * TODO put all favorites into one database
 * @author wei
 *         Date: 11/22/13
 *         Time: 9:49 AM
 */
public class FavRouteStore
{
    private static final String DB_NAME = "FavRoute";
    private static final int VERSION = 1;
    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE " + FavRoute.DB.TABLE_NAME + " (" +
        FavRoute.DB._ID + " INTEGER PRIMARY KEY, " +
        FavRoute.DB.ID + " INTEGER UNIQUE, " +
        FavRoute.DB.NAME + " TEXT" +
        " )";
    private static final String DROP_TABLE_SQL =
        "DROP TABLE IF EXISTS " + FavRoute.DB.TABLE_NAME;
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    public FavRouteStore(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        this.databaseHelper = new DatabaseHelper(context);
        this.database = databaseHelper.getWritableDatabase();
    }

    public void close()
    {
        // do nothing
    }

    /**
     * Returns a database helper for testing
     * @return the database helper for this store
     */
    DatabaseHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

    static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(DROP_TABLE_SQL);
            db.execSQL(CREATE_TABLE_SQL);
        }
    }
}

