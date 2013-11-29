package com.weizilla.transit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.common.collect.Lists;
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
            Group.GroupsStopsDB.STOP_ID + " INTEGER, " +
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
            "gs." + Group.GroupsStopsDB.STOP_ID + " " +
            "FROM " + Group.DB.TABLE_NAME + " g " +
            "LEFT JOIN " + Group.GroupsStopsDB.TABLE_NAME + " gs ON " +
            "g." + Group.DB._ID + " = " + " gs." + Group.GroupsStopsDB.GROUP_ID;
    private static final int ERROR_ID = -1;
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

    /**
     * Adds a new group to the store and returns it's
     * key id. If a group with the same name exists already,
     * returns that group's id
     * @param groupName the name of the group to be added
     * @return the key of the newly inserted group
     * or existing duplicate group
     */
    public long addGroup(String groupName)
    {
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
            id = queryGroupId(groupName);
            Log.d(TAG, "Added group already exists. id: " + id + " group: " + groupName);
        }

        return id;
    }

    public boolean removeGroup(String groupName)
    {
        long groupId = queryGroupId(groupName);

        String where = Group.DB._ID + " = ?";
        String[] whereArgs = {String.valueOf(groupId)};
        int numDeleted = db.delete(Group.DB.TABLE_NAME, where, whereArgs);
        if (numDeleted > 0)
        {
            Log.w(TAG, "Multiple rows deleted when removing group: " + groupName);
        }
        else if (numDeleted == 1)
        {
            Log.d(TAG, "Removed group: " + groupName);
        }
        else
        {
            Log.i(TAG, "No rows removed when removing group: " + groupName);
        }

        removeStopsForGroup(groupId);

        return numDeleted != 0;
    }

//    public boolean removeGroup(long groupId)
//    {
//        String where = Group.DB._ID + " = ?";
//        String[] whereArgs = {String.valueOf(groupId)};
//        int numDeleted = db.delete(Group.DB.TABLE_NAME, where, whereArgs);
//        if (numDeleted > 0)
//        {
//            Log.w(TAG, "Multiple rows deleted when removing group id: " + groupId);
//        }
//        else if (numDeleted == 1)
//        {
//            Log.d(TAG, "Removed group id: " + groupId);
//        }
//        else
//        {
//            Log.i(TAG, "No rows removed when removing group id: " + groupId);
//        }
//
//        removeStopsForGroup(groupId);
//
//        return numDeleted != 0;
//    }

    private void removeStopsForGroup(long groupId)
    {
        String where = Group.GroupsStopsDB.GROUP_ID + " = ?";
        String[] whereArgs = {String.valueOf(groupId)};
        db.delete(Group.GroupsStopsDB.TABLE_NAME, where, whereArgs);
    }

    /**
     * Adds a Stop to the group, creating a new Group if necessary.
     * Returns the id of the group the stop was added to. Returns
     * -1 if an error occured while adding a group or stop
     * @param groupName the name of the group for this stop
     * @param stop the stop to add
     * @return the id of the group, -1 if error occurred
     */
    public long addStop(String groupName, Stop stop)
    {
        if (stop.getId() == ERROR_ID)
        {
            return ERROR_ID;
        }

        long groupId = addGroup(groupName);
        if (groupId == ERROR_ID)
        {
            return ERROR_ID;
        }

        ContentValues values = new ContentValues();
        values.put(Group.GroupsStopsDB.GROUP_ID, groupId);
        values.put(Group.GroupsStopsDB.STOP_ID, stop.getId());

        long newId = db.replace(Group.GroupsStopsDB.TABLE_NAME, null, values);
        if (newId != ERROR_ID)
        {
            return groupId;
        }
        else
        {
            Log.w(TAG, "Error adding stop to group. Group id: " + groupId + " stop: " + stop);
            return ERROR_ID;
        }
    }

    public boolean removeStop(String groupName, Stop stop)
    {
        String where = Group.GroupsStopsDB.GROUP_ID + " = ? AND " +
                Group.GroupsStopsDB.STOP_ID + " = ?";
        long groupId = queryGroupId(groupName);
        if (groupId == ERROR_ID)
        {
            Log.w(TAG, "Can't remove stop from non-existant group:" + groupName);
            return false;
        }
        String[] whereArgs = {String.valueOf(groupId), String.valueOf(stop.getId())};
        int numDel = db.delete(Group.GroupsStopsDB.TABLE_NAME, where, whereArgs);
        if (numDel == 0)
        {
            Log.w(TAG, "No rows deleted when removing stop. Group: " + groupName + " Stop: " + stop);
        }
        return numDel != 0;
    }

    public List<Group> getGroups()
    {
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

    private long queryGroupId(String groupName)
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

    /**
     * Returns the database helper for this store for testing
     * purposes
     * @return the database helper for this store
     */
    SqliteDbHelper getDatabaseHelper()
    {
        return dbHelper;
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
        if (stopIdIndex != -1 && ! cursor.isNull(stopIdIndex))
        {
            group.addStopId(cursor.getInt(stopIdIndex));
        }
    }
}
