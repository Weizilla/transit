package com.weizilla.transit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.weizilla.transit.data.FavStop;

/**
 * Handles all CRUD operations related to favorite bus stops
 *
 * @author wei
 *         Date: 11/24/13
 *         Time: 6:22 PM
 */
public class FavStopStore
{
    private static final String TAG = "FavStopStore";
    private static final String DB_NAME = "FavStop";
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + FavStop.DB.TABLE_NAME + " (" +
            FavStop.DB._ID + " INTEGER PRIMARY KEY, " +
            FavStop.DB.ID + " INTEGER UNIQUE, " +
            FavStop.DB.NAME + " TEXT, " +
            FavStop.DB.ROUTE_ID + " TEXT, " +
            FavStop.DB.ROUTE_DIR + " TEXT " +
            ")";
    private static final String DROP_TABLE_SQL =
            "DROP TABLE IF EXISTS " + FavStop.DB.TABLE_NAME;
    private Context context;
    private SQLiteDatabase database;
    private SqliteDbHelper databaseHelper;

    public FavStopStore(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        databaseHelper = new SqliteDbHelper(
                context, DB_NAME, CREATE_TABLE_SQL, DROP_TABLE_SQL);
        database = databaseHelper.getWritableDatabase();
        Log.i(TAG, "Database " + DB_NAME + " open successful: " + (database != null));
    }

    public void close()
    {
        databaseHelper.close();
    }

    SqliteDbHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

}
