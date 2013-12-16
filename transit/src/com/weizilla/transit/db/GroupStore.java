package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weizilla.transit.TransitApplication;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String TAG = "transit.GroupStore";
    private static final String CREATE_GROUPS_SQL =
            "CREATE TABLE " + Group.DB.TABLE_NAME + " (" +
            Group.DB._ID + " INTEGER PRIMARY KEY, " +
            Group.DB.NAME + " TEXT UNIQUE " +
            ")";
    private static final String CREATE_GROUPS_STOPS_SQL =
            "CREATE TABLE " + Group.GroupsStopsDB.TABLE_NAME + " (" +
            Group.DB._ID + " INTEGER PRIMARY KEY, " +
            Group.GroupsStopsDB.GROUP_ID + " INTEGER, " +
            Group.GroupsStopsDB.STOP_ID + " INTEGER, " +
            Group.GroupsStopsDB.STOP_NAME + " TEXT, " +
            "UNIQUE (" + Group.GroupsStopsDB.GROUP_ID + ", " +
                    Group.GroupsStopsDB.STOP_ID + ")" +
            ")";
    private static final List<String> CREATE_SQLS =
            Lists.newArrayList(CREATE_GROUPS_SQL, CREATE_GROUPS_STOPS_SQL);
    private static final String DROP_GROUPS_SQL =
            "DROP TABLE IF EXIST " + Group.DB.TABLE_NAME;
    private static final String DROP_GROUPS_STOPS_SQL =
            "DROP TABLE IF EXIST " + Group.GroupsStopsDB.TABLE_NAME;
    private static final List<String> DROP_SQLS =
            Lists.newArrayList(DROP_GROUPS_SQL, DROP_GROUPS_STOPS_SQL);
    private static final String SELECT_GROUPS_SQL =
            "SELECT g." + Group.DB._ID + ", " +
            "g." + Group.DB.NAME + ", " +
            "gs." + Group.GroupsStopsDB.STOP_ID + ", " +
            "gs." + Group.GroupsStopsDB.STOP_NAME + " " +
            "FROM " + Group.DB.TABLE_NAME + " g " +
            "LEFT JOIN " + Group.GroupsStopsDB.TABLE_NAME + " gs ON " +
            "g." + Group.DB._ID + " = " + " gs." + Group.GroupsStopsDB.GROUP_ID;
    private static final int ERROR_ID = -1;
    private Context context;
    private SqliteDbHelper dbHelper;
    private SQLiteDatabase db;
    private String dbName = "Groups";

    public GroupStore(Context context)
    {
        this.context = context;
        TransitApplication app = (TransitApplication) context.getApplicationContext();
        dbName = app.getDbNamePrefix() + dbName;
    }

    public void open()
    {
        Log.d(TAG, "Opening Group Store db");
        dbHelper = new SqliteDbHelper(context, dbName,
                CREATE_SQLS, DROP_SQLS);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Adds a new group to the store and returns it's
     * key id. If a group with the same name exists already,
     * returns that group's id
     * @param groupName the name of the group to be added
     * @return the key of the newly inserted group
     * or existing duplicate group
     */
    public long createGroup(String groupName)
    {
        if (Strings.isNullOrEmpty(groupName))
        {
            Log.w(TAG, "Cannot create group with null or empty name");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(Group.DB.NAME, groupName);
        long id;
        try
        {
            id = db.insertOrThrow(Group.DB.TABLE_NAME, null, values);
            Log.d(TAG, "Inserted new group. id: " + id + " group: " + groupName);
        }
        catch (SQLiteConstraintException e)
        {
            // ignore duplicate name and return id of old row
            id = getGroupId(groupName);
            Log.d(TAG, "Added group already exists. id: " + id + " group: " + groupName);
        }

        return id;
    }

    public boolean createGroup(long groupId, String groupName)
    {
        assertValidGroupId(groupId);
        ContentValues values = new ContentValues();
        values.put(Group.DB._ID, groupId);
        values.put(Group.DB.NAME, groupName);

        long newId = db.replace(Group.DB.TABLE_NAME, null, values);
        return newId != -1 && newId == groupId;
    }

    public boolean removeGroup(long groupId)
    {
        assertValidGroupId(groupId);
        String where = Group.DB._ID + " = ?";
        String[] whereArgs = {String.valueOf(groupId)};
        int numDeleted = db.delete(Group.DB.TABLE_NAME, where, whereArgs);
        Log.d(TAG, "Remove Group " + groupId + ". Deleted " + numDeleted + " rows");
        removeStopsForGroup(groupId);
        return numDeleted != 0;
    }

    private void removeStopsForGroup(long groupId)
    {
        String where = Group.GroupsStopsDB.GROUP_ID + " = ?";
        String[] whereArgs = {String.valueOf(groupId)};
        db.delete(Group.GroupsStopsDB.TABLE_NAME, where, whereArgs);
    }

    public boolean addStop(long groupId, Stop stop)
    {
        assertValidGroupId(groupId);
        assertValidStop(stop);

        ContentValues values = new ContentValues();
        values.put(Group.GroupsStopsDB.GROUP_ID, groupId);
        values.put(Group.GroupsStopsDB.STOP_ID, stop.getId());
        values.put(Group.GroupsStopsDB.STOP_NAME, stop.getName());

        long newId = db.replace(Group.GroupsStopsDB.TABLE_NAME, null, values);
        if (newId == ERROR_ID)
        {
            Log.w(TAG, "Error adding stop to group. Group id: " + groupId + " stop: " + stop);
        }
        return newId != ERROR_ID;
    }

    public boolean removeStop(long groupId, Stop stop)
    {
        assertValidGroupId(groupId);
        assertValidStop(stop);

        String where = Group.GroupsStopsDB.GROUP_ID + " = ? AND " +
                Group.GroupsStopsDB.STOP_ID + " = ?";
        String[] whereArgs = {String.valueOf(groupId), String.valueOf(stop.getId())};
        int numDel = db.delete(Group.GroupsStopsDB.TABLE_NAME, where, whereArgs);
        if (numDel == 0)
        {
            Log.w(TAG, "No rows deleted when removing stop. Group id: " + groupId + " Stop: " + stop);
        }
        return numDel != 0;
    }

    public List<Group> getGroups()
    {
        if (! db.isOpen())
        {
            Log.w(TAG, "Database closed");
            return Collections.emptyList();
        }

        Cursor cursor = db.rawQuery(SELECT_GROUPS_SQL, null);
        try
        {
            return buildGroups(cursor);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public Group getGroup(long groupId)
    {
        assertValidGroupId(groupId);
        String sql = SELECT_GROUPS_SQL +
                " WHERE g." + Group.DB._ID + " = ?";
        String[] selectArgs = {String.valueOf(groupId)};
        Cursor cursor = db.rawQuery(sql, selectArgs);
        try
        {
            List<Group> groups = buildGroups(cursor);
            if (groups.isEmpty())
            {
                Log.w(TAG, "No group found with id: " + groupId);
                return null;
            }
            else
            {
                return groups.get(0);
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private long getGroupId(String groupName)
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {groupName};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols, selection, selectionArgs,
                null, null, null);
        try
        {
            List<Group> groups = buildGroups(cursor);
            return groups.isEmpty() ? ERROR_ID : groups.get(0).getId();
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
        Log.d(TAG, "Closing group store db");
        db.close();
        dbHelper.close();
    }

    public void deleteDb()
    {
        boolean status = context.deleteDatabase(dbName);
        Log.i(TAG, "Database " + dbName + " deleted successfully: " + status);
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

    private static void assertValidGroupId(long groupId)
    {
        if (groupId == ERROR_ID)
        {
            throw new IllegalArgumentException("Invalid group id: " + groupId);
        }
    }

    private static void assertValidStop(Stop stop)
    {
        if (stop == null)
        {
            throw new IllegalArgumentException("Stop is null");
        }
        if (stop.getId() == ERROR_ID)
        {
            throw new IllegalArgumentException("Invalid stop id. Stop: " + stop);
        }
    }

    private static List<Group> buildGroups(Cursor cursor)
    {
        if (cursor == null || cursor.getCount() == 0)
        {
            return Collections.emptyList();
        }

        Map<Long, Group> groups = new HashMap<>();

        cursor.moveToFirst();
        do
        {
            addRowToGroups(cursor, groups);
        }
        while (cursor.moveToNext());

        List<Group> groupList = new ArrayList<>(groups.values());
        Collections.sort(groupList);
        return groupList;
    }

    private static void addRowToGroups(Cursor cursor, Map<Long, Group> groups)
    {
        long groupId = cursor.getLong(cursor.getColumnIndexOrThrow(Group.DB._ID));
        Group group = groups.get(groupId);
        if (group == null)
        {
            String groupName = cursor.getString(cursor.getColumnIndexOrThrow(Group.DB.NAME));
            group = new Group(groupId, groupName);
            groups.put(groupId, group);
        }

        int stopIdIndex = cursor.getColumnIndex(Group.GroupsStopsDB.STOP_ID);
        int stopNameIndex = cursor.getColumnIndex(Group.GroupsStopsDB.STOP_NAME);

        if (stopIdIndex != -1 && stopNameIndex != -1 &&
            ! cursor.isNull(stopIdIndex) && ! cursor.isNull(stopNameIndex))
        {
            int stopId = cursor.getInt(stopIdIndex);
            String stopName = cursor.getString(stopNameIndex);
            Stop stop = new Stop(stopId, stopName, false);
            group.addStop(stop);
        }

    }
}
