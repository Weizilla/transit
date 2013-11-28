package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * responsible for handling grouping of bus stops so that they
 * can be polled for predictions at the same time
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:01 PM
 */
public class GroupStore
{
    private static final String TAG = "tansit.GroupStore";
    private static final String DB_NAME = "Groups";
    private static final String CREATE_GROUPS_SQL =
            "CREATE TABLE " + Group.DB.TABLE_NAME + " (" +
            Group.DB._ID + " INTEGER PRIMARY KEY, " +
            Group.DB.NAME + " TEXT UNIQUE " +
            ")";
    private static final String CREATE_GROUPS_STOPS_SQL =
            "CREATE TABLE " + Group.GroupsStopsDB.TABLE_NAME + " (" +
            Group.DB._ID + " INTEGER PRIMARY KEY, " +
            Group.GroupsStopsDB.GROUP_ID + " INTEGER, " +
            Group.GroupsStopsDB.STOP_ID + " INTEGER " +
            ")";
    private static final List<String> CREATE_SQLS =
            Lists.newArrayList(CREATE_GROUPS_SQL, CREATE_GROUPS_STOPS_SQL);
    private static final String DROP_GROUPS_SQL =
            "DROP TABLE IF EXIST " + Group.DB.TABLE_NAME;
    private static final String DROP_GROUPS_STOPS_SQL =
            "DROP TABLE IF EXIST " + Group.GroupsStopsDB.TABLE_NAME;
    private static final List<String> DROP_SQLS =
            Lists.newArrayList(DROP_GROUPS_SQL, DROP_GROUPS_STOPS_SQL);
    private Context context;
    private SqliteDbHelper dbHelper;
    private SQLiteDatabase db;

    public GroupStore(Context context)
    {
        this.context = context;

        dbHelper = new SqliteDbHelper(context, DB_NAME,
                CREATE_SQLS, DROP_SQLS);
    }

    public void open()
    {
        db = dbHelper.getWritableDatabase();
    }

    public long addGroup(Group group)
    {
        ContentValues values = new ContentValues();
        values.put(Group.DB.NAME, group.getName());
        long id;
        try
        {
            id = db.insertOrThrow(Group.DB.TABLE_NAME, null, values);
            Log.d(TAG, "Inserted new group. id: " + id + " group: " + group);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            // assume it's duplicate name error.
            // look up duplicate id and return
            id = queryGroupId(group);
            Log.d(TAG, "Added group already exists. id: " + id + " group: " + group);
        }

        return id;
    }

    public void close()
    {
        db.close();
        dbHelper.close();
    }

    public void deleteDb()
    {
        boolean status = context.deleteDatabase(DB_NAME);
        Log.i(TAG, "Database " + DB_NAME + " deleted successfully: " + status);
    }

    private long queryGroupId(Group group)
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {group.getName()};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols, selection, selectionArgs,
                null, null, null);

        List<Group> groups = buildGroups(cursor);

        return groups.get(0).getId();
    }

    /**
     * Returns the database helper for this store for testing
     * purposes
     * @return the database helper for this store
     */
    SqliteDbHelper getDatabaseHelper()
    {
        return dbHelper;
    }

    //TODO also build stop id mappings?
    private static List<Group> buildGroups(Cursor cursor)
    {
        List<Group> groups = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
        {
            return groups;
        }

        cursor.moveToFirst();
        do
        {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Group.DB._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Group.DB.NAME));
            groups.add(new Group(id, name));
        }
        while (cursor.moveToNext());

        cursor.close();

        return groups;
    }
}
