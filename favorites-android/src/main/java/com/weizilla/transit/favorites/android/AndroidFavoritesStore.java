package com.weizilla.transit.favorites.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.favorites.android.db.Favorites.RouteEntry;
import com.weizilla.transit.favorites.android.db.Favorites.StopEntry;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class AndroidFavoritesStore implements BusFavoritesStore
{
    private static final String TAG = "AndroidFavoritesStore";
    private SQLiteOpenHelper dbHelper;

    public AndroidFavoritesStore(SQLiteOpenHelper dbHelper)
    {
        this.dbHelper = dbHelper;
    }

    @Override
    public void saveFavorite(Route route)
    {
        try
        (
            SQLiteDatabase db = dbHelper.getWritableDatabase()
        )
        {
            ContentValues values = new ContentValues();
            values.put(RouteEntry.ID, route.getId());
            values.put(RouteEntry.NAME, route.getName());
            long newId = db.replace(RouteEntry.TABLE_NAME, null, values);
            if (newId != -1)
            {
                Log.d(TAG, "Added new fav route. _ID: " + newId + " Route: " + route);
            }
            else
            {
                Log.d(TAG, "Failed to add new fav route for " + route);
            }
        }
    }

    @Override
    public Collection<String> getFavoriteRouteIds()
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
    public void saveFavorite(Stop stop)
    {
        try
        (
            SQLiteDatabase db = dbHelper.getWritableDatabase();
        )
        {
            ContentValues values = new ContentValues();
            values.put(StopEntry.ID, stop.getId());
            values.put(StopEntry.ROUTE, stop.getRouteId());
            values.put(StopEntry.DIRECTION, stop.getDirection().name());
            long newId = db.replace(StopEntry.TABLE_NAME, null, values);
            if (newId != -1)
            {
                Log.d(TAG, "Added new fav route. _ID: " + newId + " Stop: " + stop);
            }
            else
            {
                Log.d(TAG, "Failed to add new fav route for " + stop);
            }
        }

    }

    @Override
    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        Set<Integer> ids = new TreeSet<>();
        try
        (
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = queryForStopIds(db, route, direction);
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
