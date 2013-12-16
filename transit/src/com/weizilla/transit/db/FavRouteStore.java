package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.weizilla.transit.BusRoutesProvider;
import com.weizilla.transit.TransitApplication;
import com.weizilla.transit.data.FavRoute;
import com.weizilla.transit.data.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles all CRUD operations related to favorite routes
 * TODO switch to content provider
 * TODO put all favorites into one database
 * @author wei
 *         Date: 11/22/13
 *         Time: 9:49 AM
 */
public class FavRouteStore implements BusRoutesProvider
{
    private static final String TAG = "transit.FavRouteStore";
    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE " + FavRoute.DB.TABLE_NAME + " (" +
        FavRoute.DB._ID + " INTEGER PRIMARY KEY, " +
        FavRoute.DB.ID + " TEXT UNIQUE, " +
        FavRoute.DB.NAME + " TEXT" +
        " )";
    private static final String DROP_TABLE_SQL =
        "DROP TABLE IF EXISTS " + FavRoute.DB.TABLE_NAME;
    private String dbName = "FavRoute";
    private Context context;
    private SQLiteDatabase database;
    private SqliteDbHelper databaseHelper;

    public FavRouteStore(Context context)
    {
        this.context = context;
        TransitApplication app = (TransitApplication) context.getApplicationContext();
        dbName = app.getDbNamePrefix() + dbName;
    }

    public void open()
    {
        databaseHelper = new SqliteDbHelper(context,
                dbName, CREATE_TABLE_SQL, DROP_TABLE_SQL);
        database = databaseHelper.getWritableDatabase();
        Log.d(TAG, "Database " + dbName + " open successful: " + (database != null));
    }

    public long addRoute(Route route)
    {
        if (! database.isOpen())
        {
            Log.i(TAG, "Database closed");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(FavRoute.DB.ID, route.getId());
        values.put(FavRoute.DB.NAME, route.getName());
        long newId = database.replace(FavRoute.DB.TABLE_NAME, null, values);
        if (newId != -1)
        {
            Log.d(TAG, "Added new fav route. _ID: " + newId + " Route: " + route);
        }
        return newId;
    }

    public boolean removeRoute(Route route)
    {
        if (! database.isOpen())
        {
            Log.w(TAG, "Database closed");
            return false;
        }

        String where = FavRoute.DB.ID + " = ?";
        String[] whereArgs = {route.getId()};
        int numDel = database.delete(FavRoute.DB.TABLE_NAME, where, whereArgs);
        Log.d(TAG, "Removing route " + route.getName() + ". Num of rows deleted: " + numDel);
        return numDel != 0;
    }

    public List<Route> getRoutes()
    {
        if (! database.isOpen())
        {
            Log.w(TAG, "Database closed");
            return Collections.emptyList();
        }

        String[] cols = {FavRoute.DB.ID, FavRoute.DB.NAME};
        String order = FavRoute.DB.ID + " ASC";
        Cursor cursor = database.query(FavRoute.DB.TABLE_NAME, cols, null, null, null, null, order);
        try
        {
            return buildRoutes(cursor);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public void close()
    {
        Log.d(TAG, "Fav Store db closed");
        database.close();
        databaseHelper.close();
    }

    public void deleteDb()
    {
        boolean status = context.deleteDatabase(dbName);
        Log.i(TAG, "Database " + dbName + " delete successful: " + status);
    }

    /**
     * Returns a database helper for testing
     * @return the database helper for this store
     */
    SqliteDbHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

    private static List<Route> buildRoutes(Cursor cursor)
    {
        List<Route> routes = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
        {
            return routes;
        }

        cursor.moveToFirst();
        do
        {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(FavRoute.DB.ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FavRoute.DB.NAME));
            routes.add(new Route(id, name, true));
        }
        while (cursor.moveToNext());
        return routes;
    }

}

