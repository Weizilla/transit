package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.FavStop;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all CRUD operations related to favorite bus stops
 * TODO switch to content provider
 * TODO put all favorites into one database
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

    public long addStop(Route route, Direction direction, Stop stop)
    {
        ContentValues values = new ContentValues();
        values.put(FavStop.DB.ID, stop.getId());
        values.put(FavStop.DB.NAME, stop.getName());
        values.put(FavStop.DB.ROUTE_ID, route.getId());
        values.put(FavStop.DB.ROUTE_DIR, direction.toString());

        long newId = database.replace(FavStop.DB.TABLE_NAME, null, values);
        if (newId != -1)
        {
            Log.d(TAG, "Added new fav stop. _ID: " + newId + " Stop: " + stop);
        }
        return newId;
    }

    public List<Stop> getStops(Route route, Direction direction)
    {
        String[] cols = {FavStop.DB.ID, FavStop.DB.NAME};
        String order = FavStop.DB.ID + " ASC";
        String selection = FavStop.DB.ROUTE_ID + " = ? AND " +
                FavStop.DB.ROUTE_DIR + " = ?";
        String[] selectionArgs = {route.getId(), direction.toString()};
        Cursor cursor = database.query(FavStop.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, order);
        return buildStops(cursor);
    }

    public void close()
    {
        databaseHelper.close();
    }

    SqliteDbHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

    private static List<Stop> buildStops(Cursor cursor)
    {
        List<Stop> stops = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
        {
            return stops;
        }
        cursor.moveToFirst();
        do
        {
            String idStr = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.ID));
            int id = Integer.parseInt(idStr);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.NAME));
            stops.add(new Stop(id, name));
        }
        while (cursor.moveToNext());

        return stops;
    }
}