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
import com.weizilla.transit.favorites.sqlite.Favorites.RoutesEntry;
import com.weizilla.transit.favorites.sqlite.Favorites.StopsEntry;

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
            values.put(RoutesEntry.ID, route.getId());
            values.put(RoutesEntry.NAME, route.getName());
            long newId = db.replace(RoutesEntry.TABLE_NAME, null, values);
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
            Cursor cursor = db.query(RoutesEntry.TABLE_NAME, new String[]{RoutesEntry.ID},
                null, null, null, null, null)
        )
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do
                {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(RoutesEntry.ID));
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
            values.put(StopsEntry.ID, stop.getId());
            values.put(StopsEntry.ROUTE, stop.getRouteId());
            values.put(StopsEntry.DIRECTION, stop.getDirection().name());
            long newId = db.replace(StopsEntry.TABLE_NAME, null, values);
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
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(StopsEntry.ID));
                    ids.add(id);
                }
                while (cursor.moveToNext());
            }
        }
        return ids;
    }

    private static Cursor queryForStopIds(SQLiteDatabase db, String route, Direction direction)
    {
        String[] cols = {StopsEntry.ID};
        String selection = StopsEntry.ROUTE + " = ?  AND " + StopsEntry.DIRECTION + " = ?";
        String[] selectArgs = {route, direction.name()};
        return db.query(StopsEntry.TABLE_NAME, cols, selection, selectArgs, null, null, null);
    }
}
