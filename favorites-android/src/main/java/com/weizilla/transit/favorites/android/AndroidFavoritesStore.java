package com.weizilla.transit.favorites.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry;
import com.weizilla.transit.favorites.sqlite.Favorites.StopEntry;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class AndroidFavoritesStore implements FavoritesStore
{
    private static final String TAG = "AndroidFavoritesStore";
    private SQLiteOpenHelper dbHelper;

    public AndroidFavoritesStore(SQLiteOpenHelper dbHelper)
    {
        this.dbHelper = dbHelper;
    }

    @Override
    public void saveFavorite(String routeId)
    {
        try
        (
            SQLiteDatabase db = dbHelper.getWritableDatabase()
        )
        {
            ContentValues values = new ContentValues();
            values.put(RouteEntry.ID, routeId);
            long newId = db.replace(RouteEntry.TABLE_NAME, null, values);
            if (newId != -1)
            {
                Log.d(TAG, "Added new fav route. _ID: " + newId + " Route: " + routeId);
            }
            else
            {
                Log.d(TAG, "Failed to add new fav route for " + routeId);
            }
        }
    }

    @Override
    public Collection<String> getRouteIds()
    {
        Set<String> ids = new TreeSet<>();
        try
        (
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(RouteEntry.TABLE_NAME, new String[]{RouteEntry.ID},
                null, null, null, null, null)
        )
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do
                {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(RouteEntry.ID));
                    ids.add(id);
                }
                while (cursor.moveToNext());

            }
        }
        Log.d(TAG, "Found " + ids.size() + " favorite routes");
        return ids;
    }

    @Override
    public void saveFavorite(int stopId, String routeId, Direction direction)
    {
        try
        (
            SQLiteDatabase db = dbHelper.getWritableDatabase();
        )
        {
            ContentValues values = new ContentValues();
            values.put(StopEntry.ID, stopId);
            values.put(StopEntry.ROUTE, routeId);
            values.put(StopEntry.DIRECTION, direction.name());
            long newId = db.replace(StopEntry.TABLE_NAME, null, values);
            if (newId != -1)
            {
                Log.d(TAG, "Added new fav route. _ID: " + newId + " Stop: " + stopId);
            }
            else
            {
                Log.d(TAG, "Failed to add new fav route for " + stopId);
            }
        }

    }

    @Override
    public Collection<Integer> getStopIds(String routeId, Direction direction)
    {
        Set<Integer> ids = new TreeSet<>();
        try
        (
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = queryForStopIds(db, routeId, direction);
        )
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do
                {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(StopEntry.ID));
                    ids.add(id);
                }
                while (cursor.moveToNext());
            }
        }
        return ids;
    }

    private static Cursor queryForStopIds(SQLiteDatabase db, String route, Direction direction)
    {
        String[] cols = {StopEntry.ID};
        String selection = StopEntry.ROUTE + " = ?  AND " + StopEntry.DIRECTION + " = ?";
        String[] selectArgs = {route, direction.name()};
        return db.query(StopEntry.TABLE_NAME, cols, selection, selectArgs, null, null, null);
    }
}
