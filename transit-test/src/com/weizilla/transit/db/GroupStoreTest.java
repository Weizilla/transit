package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;

/**
 * test the group store
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:50 PM
 */
public class GroupStoreTest extends AndroidTestCase
{
    private static final String TEST_GROUP_NAME = "TEST_NAME";
    private static final Stop TEST_STOP = new Stop(123, "STOP NAME", false);
    private GroupStore store;
    private SqliteDbHelper dbHelper;
    private SQLiteDatabase db;

    public void setUp() throws Exception
    {
        super.setUp();
        store = new GroupStore(getContext());
        store.open();
        dbHelper = store.getDatabaseHelper();
        db = dbHelper.getWritableDatabase();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        store.close();
        store.deleteDb();
    }

    public void testPreconditions()
    {
        assertNotNull(store);
        assertNotNull(dbHelper);
        assertNotNull(db);
    }

    public void testAddGroup()
    {
        long newId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestResultInDb();
    }

    public void testDuplicateAddedReturnsSameIdWithoutErrors()
    {
        long firstId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(firstId != -1);

        long newId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        newId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestResultInDb();
    }

    public void testDeleteGroupWithName()
    {
        long newId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        assertSingleTestResultInDb();

        boolean deleted = store.removeGroup(TEST_GROUP_NAME);
        assertTrue(deleted);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(0, totalRows);
    }

    public void testDeleteGroupWithId()
    {
        long newId = store.addGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        assertSingleTestResultInDb();

        boolean deleted = store.removeGroup(newId);
        assertTrue(deleted);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(0, totalRows);
    }

    public void testRemoveGroupRemovesStops()
    {
        //TODO
    }

    public void testAddStopsToGroup()
    {
        boolean added = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(added);

        int totalGroups = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalGroups);

        assertSingleTestResultInDb();

        int totalGroupsStops = SqliteUtils.countRows(db,
                Group.GroupsStopsDB.TABLE_NAME);
        assertEquals(1, totalGroupsStops);

        long groupId = getGroupId(TEST_GROUP_NAME);

        assertGroupStopLink(groupId, TEST_STOP.getId());
    }

    public void testNoDuplicatedStopsAdded()
    {
        boolean added = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertFalse(added);
        added = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertFalse(added);

        int totalGroups = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalGroups);

        assertSingleTestResultInDb();

        int totalGroupsStops = SqliteUtils.countRows(db,
                Group.GroupsStopsDB.TABLE_NAME);
        assertEquals(1, totalGroupsStops);

        long groupId = getGroupId(TEST_GROUP_NAME);

        assertGroupStopLink(groupId, TEST_STOP.getId());
    }

    public void testRemoveStopsFromGroup()
    {
        //TODO
    }

    private Cursor queryForGroups(String groupName)
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {groupName};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);
        return cursor;
    }

    private long getGroupId(String groupName)
    {
        Cursor cursor = queryForGroups(groupName);
        try
        {
            cursor.moveToFirst();
            return cursor.getLong(cursor.getColumnIndexOrThrow(Group.DB._ID));
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private void assertSingleTestResultInDb()
    {
        Cursor cursor = queryForGroups(TEST_GROUP_NAME);
        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            String actualName = cursor.getString(cursor.getColumnIndexOrThrow(Group.DB.NAME));
            assertEquals(TEST_GROUP_NAME, actualName);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private void assertGroupStopLink(long groupId, long stopId)
    {
        String selection = Group.GroupsStopsDB.GROUP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(groupId)};
        String[] cols = {Group.GroupsStopsDB.GROUP_ID,
                Group.GroupsStopsDB.STOP_ID};
        Cursor cursor = db.query(Group.GroupsStopsDB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);
        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            int groupIdIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.GROUP_ID);
            long actualGroupId = cursor.getLong(groupIdIndex);
            assertEquals(groupId, actualGroupId);

            int stopIdIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.STOP_ID);
            long actualStopId = cursor.getLong(stopIdIndex);
            assertEquals(stopId, actualStopId);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

}
