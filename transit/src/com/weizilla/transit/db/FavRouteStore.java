package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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
public class FavRouteStore
{
    private static final String TAG = "FavRouteStore";
    private static final String DB_NAME = "FavRoute";
    private static final int VERSION = 1;
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
    private DatabaseHelper databaseHelper;

    public FavRouteStore(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        this.databaseHelper = new DatabaseHelper(context);
        this.database = databaseHelper.getWritableDatabase();
        Log.i(TAG, "Database " + DB_NAME + " open successful: " + (database != null));
    }

    public long addRoute(Route route)
    {
        ContentValues values = new ContentValues();
        values.put(FavRoute.DB.ID, route.getId());
        values.put(FavRoute.DB.NAME, route.getName());
        return database.replace(FavRoute.DB.TABLE_NAME, null, values);
    }

    public List<Route> getRoutes()
    {
        String[] cols = {FavRoute.DB.ID, FavRoute.DB.NAME};
        String order = FavRoute.DB.ID + " ASC";
        Cursor cursor = database.query(FavRoute.DB.TABLE_NAME, cols, null, null, null, null, order);
        return makeRoute(cursor);
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
    DatabaseHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

    private static List<Route> makeRoute(Cursor cursor)
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
            routes.add(new Route(id, name));
        }
        while (cursor.moveToNext());
        return routes;
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

