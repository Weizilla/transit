package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.util.SqliteTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * test the group store
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:50 PM
 */
public class GroupStoreTest extends AndroidTestCase
{
    private static final long TEST_GROUP_ID = 100;
    private static final long TEST_GROUP_ID_2 = 200;
    private static final String TEST_GROUP_NAME = "TEST_NAME";
    private static final String TEST_GROUP_NAME_2 = "TEST_NAME_2";
    private static final Stop TEST_STOP = new Stop(123, "STOP_NAME", false);
    private static final Stop TEST_STOP_2 = new Stop(234, "STOP_NAME_2", false);
    private static final Stop TEST_STOP_3 = new Stop(345, "STOP_NAME_3", false);
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

    public void testAddGroupWithName()
    {
        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(newId);
    }

    public void testAddGroupWithIdAndName()
    {
        long groupId = 100;
        boolean added = store.createGroup(groupId, TEST_GROUP_NAME);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(groupId);
    }

    public void testAddGroupWithDiffIdAndSameNameReplacesPrev()
    {
        long groupId = 100;
        boolean added = store.createGroup(groupId, TEST_GROUP_NAME);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(groupId);

        long newGroupId = 200;
        added = store.createGroup(newGroupId, TEST_GROUP_NAME);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(newGroupId);
    }

    public void testDuplicateAddedReturnsSameIdWithoutErrors()
    {
        long firstId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(firstId != -1);

        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);

        assertSingleTestResultInDb(firstId);
    }

    public void testDeleteGroupWithName()
    {
        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        assertSingleTestResultInDb(newId);

        boolean deleted = store.removeGroup(newId);
        assertTrue(deleted);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 0);
    }

    public void testRemoveGroupRemovesStops()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP_2);
        assertTrue(added);
        assertSingleTestResultInDb(TEST_GROUP_ID);
        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 2);

        boolean deleted = store.removeGroup(TEST_GROUP_ID);
        assertTrue(deleted);
        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 0);
    }

    public void testAddStopsToGroup()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(TEST_GROUP_ID);

        SqliteTestUtils.assertRowCount(db,Group.GroupsStopsDB.TABLE_NAME, 1);
        assertGroupStopLink(TEST_GROUP_ID, TEST_STOP);
    }

    public void testNoDuplicatedStopsAdded()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(TEST_GROUP_ID);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 1);
        assertGroupStopLink(TEST_GROUP_ID, TEST_STOP);
    }

    //TODO test foreign key constraint between group id and
    // group id in linked table

    public void testRemoveStopsFromGroup()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);

        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP_2);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb(TEST_GROUP_ID);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 2);
        assertGroupStopLink(TEST_GROUP_ID, TEST_STOP);
        assertGroupStopLink(TEST_GROUP_ID, TEST_STOP_2);

        boolean removed = store.removeStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(removed);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 1);
        assertGroupStopLink(TEST_GROUP_ID, TEST_STOP_2);

        removed = store.removeStop(TEST_GROUP_ID, TEST_STOP_2);
        assertTrue(removed);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 0);
        assertSingleTestResultInDb(TEST_GROUP_ID);
    }

    public void testGetGroupWithId()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP_2);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP_3);
        assertTrue(added);
        List<Stop> stops = Lists.newArrayList(TEST_STOP, TEST_STOP_2, TEST_STOP_3);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 3);

        Group actualGroup = store.getGroup(TEST_GROUP_ID);
        assertEquals(TEST_GROUP_ID, actualGroup.getId());
        assertEquals(TEST_GROUP_NAME, actualGroup.getName());
        assertEquals(stops, actualGroup.getStops());
    }

    public void testGetGroupsWithNoStops()
    {
        boolean added = store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        assertTrue(added);
        added = store.createGroup(TEST_GROUP_ID_2, TEST_GROUP_NAME_2);
        assertTrue(added);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 2);

        List<Group> groups = store.getGroups();
        assertEquals(2, groups.size());

        Group group1 = groups.get(0);
        assertEquals(TEST_GROUP_ID, group1.getId());
        assertEquals(TEST_GROUP_NAME, group1.getName());
        assertTrue(group1.getStops().isEmpty());

        Group group2 = groups.get(1);
        assertEquals(TEST_GROUP_ID_2, group2.getId());
        assertEquals(TEST_GROUP_NAME_2, group2.getName());
        assertTrue(group2.getStops().isEmpty());
    }

    public void testGetGroupsWithStops()
    {
        store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        boolean added = store.addStop(TEST_GROUP_ID, TEST_STOP);
        assertTrue(added);
        added = store.addStop(TEST_GROUP_ID, TEST_STOP_2);
        assertTrue(added);
        List<Stop> expected1 = new ArrayList<>();
        Collections.addAll(expected1, TEST_STOP, TEST_STOP_2);
        Collections.sort(expected1);

        store.createGroup(TEST_GROUP_ID_2, TEST_GROUP_NAME_2);
        added = store.addStop(TEST_GROUP_ID_2, TEST_STOP);
        assertTrue(added);
        store.addStop(TEST_GROUP_ID_2, TEST_STOP_2);
        store.addStop(TEST_GROUP_ID_2, TEST_STOP_3);
        List<Stop> expected2 = new ArrayList<>();
        Collections.addAll(expected2, TEST_STOP, TEST_STOP_2, TEST_STOP_3);
        Collections.sort(expected2);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 5);

        List<Group> groups = store.getGroups();
        assertEquals(2, groups.size());
        Collections.sort(groups);

        Group group1 = groups.get(0);
        assertEquals(TEST_GROUP_ID, group1.getId());
        assertEquals(TEST_GROUP_NAME, group1.getName());
        List<Stop> stops1 = group1.getStops();
        Collections.sort(stops1);
        assertEquals(expected1, stops1);

        Group group2 = groups.get(1);
        assertEquals(TEST_GROUP_ID_2, group2.getId());
        assertEquals(TEST_GROUP_NAME_2, group2.getName());
        List<Stop> stops2 = group2.getStops();
        Collections.sort(stops2);
        assertEquals(expected2, stops2);
    }

    private void assertSingleTestResultInDb(long groupId)
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {TEST_GROUP_NAME};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);
        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            long actualGroupId = cursor.getLong(cursor.getColumnIndexOrThrow(Group.DB._ID));
            assertEquals(groupId, actualGroupId);

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

    private void assertGroupStopLink(long groupId, Stop stop)
    {
        String stopId = String.valueOf(stop.getId());
        String selection = Group.GroupsStopsDB.GROUP_ID + " = ? AND " +
                Group.GroupsStopsDB.STOP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(groupId), stopId};
        String[] cols = {Group.GroupsStopsDB.GROUP_ID,
                Group.GroupsStopsDB.STOP_ID,
                Group.GroupsStopsDB.STOP_NAME};
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
            assertEquals(stop.getId(), actualStopId);

            int stopNameIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.STOP_NAME);
            String actualStopName = cursor.getString(stopNameIndex);
            assertEquals(stop.getName(), actualStopName);
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
