package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.weizilla.transit.BusRoutesProvider;
import com.weizilla.transit.data.FavRoute;
import com.weizilla.transit.data.Route;

import java.util.ArrayList;
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
    private static final String DB_NAME = "FavRoute";
    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE " + FavRoute.DB.TABLE_NAME + " (" +
        FavRoute.DB._ID + " INTEGER PRIMARY KEY, " +
        FavRoute.DB.ID + " TEXT UNIQUE, " +
        FavRoute.DB.NAME + " TEXT" +
        " )";
    private static final String DROP_TABLE_SQL =
        "DROP TABLE IF EXISTS " + FavRoute.DB.TABLE_NAME;
    private Context context;
    private SQLiteDatabase database;
    private SqliteDbHelper databaseHelper;

    public FavRouteStore(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        databaseHelper = new SqliteDbHelper(context,
            DB_NAME, CREATE_TABLE_SQL, DROP_TABLE_SQL);
        database = databaseHelper.getWritableDatabase();
        Log.i(TAG, "Database " + DB_NAME + " open successful: " + (database != null));
    }

    public long addRoute(Route route)
    {
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
        String where = FavRoute.DB.ID + " = ?";
        String[] whereArgs = {route.getId()};

        int numDel = database.delete(FavRoute.DB.TABLE_NAME, where, whereArgs);

        if (numDel > 1)
        {
            Log.w(TAG, "Multiple rows deleted for single route. Num: " + numDel + " Route: " + route);
        }
        else if (numDel == 1)
        {
            Log.d(TAG, "Deleted fav route: " + route);
        }
        else
        {
            Log.i(TAG, "No rows delete for route: " + route);
        }

        return numDel != 0;
    }

    public List<Route> getRoutes()
    {
        String[] cols = {FavRoute.DB.ID, FavRoute.DB.NAME};
        String order = FavRoute.DB.ID + " ASC";
        Cursor cursor = database.query(FavRoute.DB.TABLE_NAME, cols, null, null, null, null, order);
        return buildRoutes(cursor);
    }

    public void close()
    {
        this.databaseHelper.close();
    }

    public void deleteDb()
    {
        boolean status = context.deleteDatabase(DB_NAME);
        Log.i(TAG, "Database " + DB_NAME + " delete successful: " + status);
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

